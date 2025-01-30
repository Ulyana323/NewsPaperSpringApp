package ru.khav.NewsPaper.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.DTO.CommentDTO;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.services.CommentService;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.utill.CommentSorting;
import ru.khav.NewsPaper.utill.CommentValidator;
import ru.khav.NewsPaper.utill.ErrorResponse;
import ru.khav.NewsPaper.utill.NewsValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping("/news")
@ControllerAdvice
public class CommentController {

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

    //метод принимает текст комментария и заголовок для какой новости коммент
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@RequestBody @Valid CommentDTO commentDTO,
                                        @RequestParam("title") String NewsTitle,
                                        BindingResult bindingResult) {
        commentValidator.validate(commentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (commentService.addComment(commentDTO,//выборка новости из бд по id, а выборка id по заголовку, который
                //должен быть получен с клиента
                newsService.FindByTitle(NewsTitle).getId()).equals("yep")) {
            return new ResponseEntity<>("ok", HttpStatus.OK);
        }

        return new ResponseEntity<>("nok", HttpStatus.BAD_REQUEST);
    }

    //при нажатии "еще комментарии" в теле запроса передается номер стр
    @GetMapping("/showComm")
    public List<CommentShowDTO> ShowComments(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = true, defaultValue = "title") String title) {
        return commentSorting.ShowComments(page, title);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/deleteComment")
    public int deleteComment(@RequestParam String title, @RequestParam int commentId)
    {
        return commentService.deleteComment(title,commentId);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponse("нет доступа", System.currentTimeMillis()), HttpStatus.FORBIDDEN);
    }


}
