package ru.khav.NewsPaper.Convertors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khav.NewsPaper.DTO.NewsDTO;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Themes;
import ru.khav.NewsPaper.repositories.ThemeRepo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NewsConvertor {
    @Autowired
    ThemeRepo themeRepo;

//    public NewsShowDTO convertTOShow(News news) {
//        if (news == null) {
//            return null;
//        }
//
//    }
}

