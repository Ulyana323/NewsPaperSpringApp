package ru.khav.NewsPaper.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.Convertors.CommentConverter;
import ru.khav.NewsPaper.Convertors.NewsConvertor;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.DTO.NewsDTO;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Themes;
import ru.khav.NewsPaper.repositories.NewsRepo;
import ru.khav.NewsPaper.repositories.ThemeRepo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    NewsRepo newsRepo;
    @Autowired
    ThemeRepo themeRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CommentConverter commentConverter;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsConvertor newsConvertor;

    public List<NewsShowDTO> showNewsForAuthrizedUser(int page, Person user) {
        List<News> news = newsRepo.findAllByOrderByCreatedAtDesc().orElse(null);
        if (news == null) {
            return null;
        }
        if (page == 0) {
            LocalDateTime nowDate = LocalDateTime.now();
            news = news.stream()
                    .filter(c -> c.getCreatedAt().isAfter(nowDate.minusHours(24)))
                    .limit(3)
                    .collect(Collectors.toList());
        } else {
            news = newsRepo.findAll(PageRequest.of(page, 3, Sort.by("createdAt").descending())).toList();
        }

        return news.stream()
                .map(n -> new NewsShowDTO(n.getTitle(), n.getText(),
                        CommentListTransform(n.getComments()), likeService.isNewsLikesByCurrUser(user, n), n.getLikes().size(), n.getCreatedAt(), convertListThemes(n.getThemes())))
                .collect(Collectors.toList());

    }

    public List<NewsShowDTO> showNews(int page) {
        List<News> news = newsRepo.findAllByOrderByCreatedAtDesc().orElse(null);
        if (news == null) {
            return null;
        }
        if (page == 0) {
            LocalDateTime nowDate = LocalDateTime.now();
            news = news.stream()
                    .filter(c -> c.getCreatedAt().isAfter(nowDate.minusHours(24)))
                    .limit(3)
                    .collect(Collectors.toList());
        } else {
            news = newsRepo.findAll(PageRequest.of(page, 3, Sort.by("createdAt").descending())).toList();
        }

        return news.stream()
                .map(n -> new NewsShowDTO(n.getTitle(), n.getText(),
                        CommentListTransform(n.getComments()), false, n.getLikes().size(), n.getCreatedAt(), convertListThemes(n.getThemes())))
                .collect(Collectors.toList());

    }
//чтобф не пересылать рекурскию клиенту
    public Set<String> convertListThemes(Set<Themes> themes) {
        if (themes.isEmpty()) {
            return null;
        }
        Set<String> s = new HashSet<>();
        for (Themes t : themes) {
            s.add(t.getName());
        }
        return s;
    }

    @Transactional
    public Integer showNewsAndLikeit(String title) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            News newsToLike = FindByTitle(title);
            if (newsToLike == null) {
                return 0;
            }
            likeService.like((Person) auth.getPrincipal(), newsToLike);
            return 1;
        }
        return 0;
    }

    public List<CommentShowDTO> CommentListTransform(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }
        for (Comment comment : comments) {
            if (comment.getOwner() == null) {
                logger.warn("Comment owner is null for comment: {}", comment);
            }
        }
        List<CommentShowDTO> res = new ArrayList<>();
        for (Comment c : comments) {
            res.add(commentConverter.convertToDTO(c));
        }
        return comments.stream()
                .map(commentConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional//при сохранении новых новостей проверяем соответствуют ли полученные имена тематик реальным и потом формируем связь
    public int saveNews(NewsDTO newsDTO) {
        if (newsDTO != null) {
            News news = new News();
            news.setTitle(newsDTO.getTitle());
            news.setText(newsDTO.getText());
            newsRepo.save(news);
            News savedNews = newsRepo.findByTitle(news.getTitle()).get();
            Set<Themes> themes = new HashSet<>();
            for (String i : newsDTO.getThemes()) {
                themeRepo.findByName(i).ifPresent(theme -> {
                    themes.add(theme);
                    theme.getNews().add(savedNews);
                });
            }
            savedNews.setThemes(themes);
            return 1;
        } else
            return 0;
    }

    public News findbyId(int id) {
        return newsRepo.findById(id).get();
    }

    public News FindByTitle(String title) {
        return newsRepo.findByTitle(title).orElse(null);
    }

    @Transactional
    public int editNews(NewsDTO newsDTO) {
        News oldNews = newsRepo.findById(newsDTO.getId()).get();
        if (!oldNews.getTitle().equals(newsDTO.getTitle())) {
            oldNews.setTitle(newsDTO.getTitle());
        }
        oldNews.setText(newsDTO.getText());
        //удаляем старые темы
        oldNews.getThemes().stream().forEach(x -> x.getNews().remove(oldNews));
        oldNews.getThemes().clear();
        Set<Themes> editedThemes = new HashSet<>();
        newsDTO.getThemes().stream().forEach(x -> themeRepo.findByName(x).ifPresent(editedThemes::add));
        //добавляем новые
        editedThemes.stream().forEach(x -> x.getNews().add(oldNews));
        oldNews.setThemes(editedThemes);
        return 1;
    }

    @Transactional
    public int deleteNews(String title) {
        if (FindByTitle(title) == null) {
            return -1;
        }
        newsRepo.delete(FindByTitle(title));
        return 1;
    }

}
