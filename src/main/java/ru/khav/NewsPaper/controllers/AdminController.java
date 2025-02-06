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
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.utill.ErrorResponse;
import ru.khav.NewsPaper.utill.NewsValidator;

import javax.validation.Valid;

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
        if (newsService.saveNews(newsDTO) == 1) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delNew")
    public ResponseEntity<?> DelNews(@RequestParam String title) throws AccessDeniedException {
        if (newsService.deleteNews(title) == 1) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //сначала клиент получает id новости из бд
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/editNews")
    public int getIdNews(@RequestParam String title) throws AccessDeniedException {

        return newsService.FindByTitle(title).getId();
    }

    //потом отправляет этот id с новыми данными
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/editNews")
    public ResponseEntity<?> editNews(@RequestBody @Valid NewsDTO editedNews) throws AccessDeniedException {
        if (newsService.editNews(editedNews) == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok("News updated successfully");
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponse("нет доступа", System.currentTimeMillis()), HttpStatus.FORBIDDEN);
    }


}
