package ru.khav.NewsPaper.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.services.CommentService;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.utill.CommentSorting;
import ru.khav.NewsPaper.utill.CommentValidator;
import ru.khav.NewsPaper.utill.ErrorResponse;
import ru.khav.NewsPaper.utill.NewsValidator;

import java.util.List;

@Controller
@RestController
@RequestMapping("/news")
@ControllerAdvice
public class NewsController {

    @Autowired
    NewsService newsService;
    @Autowired
    NewsValidator newsValidator;
    @Autowired
    CommentValidator commentValidator;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentSorting commentSorting;

    //метод ожидает страницу, на страницу макс 3 новости, но если
    //страница=0 то есть первая то новости только последних 24 часов отображаются, даже если их меньше 3
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/showforAuthorized/{page}")
    public ResponseEntity<List<NewsShowDTO>> showNewsAuthorized(@PathVariable("page") int page) {
        //чтобы избежать отрицательных значений
        int pag = page <= 0 ? 0 : page;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            List<NewsShowDTO> lst = newsService.showNewsForAuthrizedUser(pag, (Person) auth.getPrincipal());
            return new ResponseEntity<>(lst, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ROLE_ANONYMOUS')")
    @GetMapping("/show/{page}")
    public ResponseEntity<List<NewsShowDTO>> showNews(@PathVariable("page") int page) {
        //чтобы избежать отрицательных значений
        int pag = page <= 0 ? 0 : page;
        List<NewsShowDTO> lst = newsService.showNews(pag);
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponse("нет доступа", System.currentTimeMillis()), HttpStatus.FORBIDDEN);
    }


}
