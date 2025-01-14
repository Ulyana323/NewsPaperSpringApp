package ru.khav.NewsPaper.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.DTO.CommentDTO;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.DTO.NewsDTO;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.News;
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
@RequestMapping("/News")
@ControllerAdvice
public class MainPageController {

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

    @GetMapping("/show/{page}")//метод ожидает страницу, на страницу макс 3 новости, но если
    //страница=0 то есть первая то новости только последних 24 часов отображаются, даже если их меньше 3
    public ResponseEntity<List<NewsShowDTO>> showNews(@PathVariable("page") int page) {
        int pag = page <= 0 ? 0 : page;//чтобы избежать отрицательных значений
        List<NewsShowDTO> lst = newsService.showNews(pag);
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @PostMapping("/like")
    //метод ожидает заголовок новости (он в бд уникален) и значение лайка(поставлен/снят)
    public ResponseEntity<Integer> LikeNewsAndShow(@RequestParam String title,
                                                   @RequestParam(required = true, defaultValue = "false") boolean like) {
        Integer response = newsService.showNewsAndLikeit(title, like);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addComment")//метод принимает текст комментария и заголовок для какой новости коммент
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


    @GetMapping("/showComm")//при нажатии "еще комметнарии" в теле запроса передается номер стр
    public List<CommentShowDTO> ShowComments(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = true, defaultValue = "title") String title) {
        return commentSorting.ShowComments(page, title);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addNew")
    public ResponseEntity<?> addNews(@RequestBody @Valid NewsDTO newsDTO
            , BindingResult bindingResult) throws AccessDeniedException {
        newsValidator.validate(newsDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse("incorrect format of News", System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
        }
        if (newsService.saveNews(modelMapper.map(newsDTO, News.class)) == 1) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else return new ResponseEntity<>("Not Authorized", HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/delNew")
    public ResponseEntity<?> DelNews(@RequestParam String title) throws AccessDeniedException {
        newsService.deleteNews(title);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponse("нет доступа", System.currentTimeMillis()), HttpStatus.FORBIDDEN);
    }


}
