package ru.khav.NewsPaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.repositories.NewsRepo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    NewsRepo newsRepo;

    public List<News> showNews()
    {
        return newsRepo.findAll();
    }

    @Transactional
    public void saveNews(News news)
    {
        enrichNews(news);
        newsRepo.save(news);
    }

    private void enrichNews(News news)
    {
        news.setCreated_at(LocalDateTime.now());
    }


}
