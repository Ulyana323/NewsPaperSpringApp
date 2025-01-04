package ru.khav.NewsPaper.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.Convertors.CommentConverter;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.repositories.NewsRepo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    NewsRepo newsRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CommentConverter commentConverter;


    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    public List<NewsShowDTO> showNews(int page)
    {
        List<News> news= newsRepo.findAll(Sort.by("id"));
        if(page>news.size()-1){
            page=0;
        }
        news=newsRepo.findAll(PageRequest.of(page,1)).getContent();
        List<NewsShowDTO> NewsShowDTOS=new ArrayList<>();
        for(News n:news)
        {
            NewsShowDTOS.add(new NewsShowDTO(n.getTitle(),n.getText(),
                    CommentListTransform(n.getComments()),n.isLiked()));
        }
        return NewsShowDTOS;

    }
    @Transactional
    public Integer showNewsAndLikeit(int page,boolean like)
    {
        List<News> news= newsRepo.findAll();
        if(page>news.size()-1){
           return 0;
        }
        //int id=
       newsRepo.findAll(PageRequest.of(page,1)).getContent().get(0).setLiked(like);
        return 1;

    }


    public List<CommentShowDTO> CommentListTransform(List<Comment> comments)
    {
        if (comments == null) {
            return Collections.emptyList();
        }
        for (Comment comment : comments) {
            if (comment.getOwner() == null) {
                logger.warn("Comment owner is null for comment: {}", comment);
            }
        }
        return comments.stream()
                .map(commentConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveNews(News news) {
        newsRepo.save(news);
    }

    public News findbyId(int id)
    {
        return newsRepo.findById(id).get();
    }


    public News FindByTitle(String title){
        return newsRepo.findByTitle(title);}



}
