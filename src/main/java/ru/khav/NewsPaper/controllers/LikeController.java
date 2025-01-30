package ru.khav.NewsPaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.services.LikeService;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.utill.ErrorResponse;

@Controller
@RestController
@RequestMapping("/news")
@ControllerAdvice
public class LikeController {

    @Autowired
    NewsService newsService;
    @Autowired
    LikeService likeService;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/like")
    //метод ожидает заголовок новости (он в бд уникален)
    public ResponseEntity<Integer> LikeNewsAndShow(@RequestParam String title) {
        Integer response = newsService.showNewsAndLikeit(title);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/unlike")
    //метод ожидает заголовок новости (он в бд уникален)
    public int UnLikeNews(@RequestParam String title,@RequestParam String email) {
        System.out.println("unlike");
        return likeService.unlike(title, email);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponse("авторизируйтесь!", System.currentTimeMillis()), HttpStatus.FORBIDDEN);
    }
}
