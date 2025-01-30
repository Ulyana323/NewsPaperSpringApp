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
import ru.khav.NewsPaper.DTO.NewsDTO;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.services.CommentService;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.utill.CommentSorting;
import ru.khav.NewsPaper.utill.CommentValidator;
import ru.khav.NewsPaper.utill.ErrorResponse;
import ru.khav.NewsPaper.utill.NewsValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/admin")
@ControllerAdvice
public class AdminController {

    @Autowired
    NewsService newsService;
    @Autowired
    NewsValidator newsValidator;
    @Autowired
    ModelMapper modelMapper;

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
    @DeleteMapping("/delNew")
    public ResponseEntity<?> DelNews(@RequestParam String title) throws AccessDeniedException {
        newsService.deleteNews(title);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/editNews")
    public int getIdNews(@RequestParam String title)
    {
        return newsService.FindByTitle(title).getId();
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/editNews")
    public ResponseEntity<?> editNews(@RequestBody @Valid NewsDTO editedNews,
        BindingResult bindingResult){
        newsValidator.validate(editedNews, bindingResult);
        if (bindingResult.hasErrors()) {
            // Создаем список ошибок
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new ErrorResponse(errorMessages, System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
        }
        newsService.editNews(editedNews);
        return ResponseEntity.ok("News updated successfully");
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponse("нет доступа", System.currentTimeMillis()), HttpStatus.FORBIDDEN);
    }


}
