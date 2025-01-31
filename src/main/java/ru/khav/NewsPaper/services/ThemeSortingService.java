package ru.khav.NewsPaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.repositories.ThemeRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;

@Service
public class ThemeSortingService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    ThemeRepo themeRepo;

    public int addThemesToNews(Set<String> themes, News news)
    {
        if(themes.isEmpty())
        {
            return 0;
        }
        for(String i:themes)
        {
            if(themeRepo.findByName(i).isPresent())
            {
                news.getThemes().add(themeRepo.findByName(i).get());
            }
        }
        return 1;
    }



}
