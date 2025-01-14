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
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.repositories.NewsRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    NewsRepo newsRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CommentConverter commentConverter;

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
                        CommentListTransform(n.getComments()), n.isLiked(), n.getCreatedAt()))
                .collect(Collectors.toList());

    }

    @Transactional
    public Integer showNewsAndLikeit(String title, boolean like) {
        News NewsToLike = FindByTitle(title);
        if (NewsToLike == null) {
            return 0;
        }
        NewsToLike.setLiked(like);
        return 1;

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

    @Transactional
    public int saveNews(News news) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            newsRepo.save(news);
            return 1;
        } else return 0;
    }

    public News findbyId(int id) {
        return newsRepo.findById(id).get();
    }


    public News FindByTitle(String title) {
        return newsRepo.findByTitle(title).orElse(null);
    }


}
