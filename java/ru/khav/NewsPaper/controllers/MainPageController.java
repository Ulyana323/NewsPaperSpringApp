package ru.khav.NewsPaper.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.DTO.NewsDTO;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.utill.ErrorResponse;
import ru.khav.NewsPaper.utill.NewsValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping("/News")
public class MainPageController {

    @Autowired
    NewsService newsService;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    NewsValidator newsValidator;

    @GetMapping()
    public ResponseEntity<List<News>> showNews()
    {
        return new ResponseEntity<>(newsService.showNews(),HttpStatus.OK);
    }

    @PostMapping("/addNew")
    public ResponseEntity<?> addNews(@RequestBody @Valid NewsDTO newsDTO, BindingResult bindingResult)
    {
        newsValidator.validate(newsDTO,bindingResult);
        if(bindingResult.hasErrors())
        {
            return new ResponseEntity<>(new ErrorResponse("incorrect format of News",System.currentTimeMillis()),HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            newsService.saveNews(modelMapper.map(newsDTO,News.class));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else return new ResponseEntity<>("Not Authorized",HttpStatus.BAD_REQUEST);

    }
}
