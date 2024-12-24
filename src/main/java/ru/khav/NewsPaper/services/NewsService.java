package ru.khav.NewsPaper.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.repositories.NewsRepo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    NewsRepo newsRepo;

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    public List<News> showNews()
    {
        return newsRepo.findAll();
    }

    @Transactional
    public void saveNews(News news) {
        newsRepo.save(news);
    }

    public News findbyId(int id)
    {
        return newsRepo.findById(id).get();
    }



}
