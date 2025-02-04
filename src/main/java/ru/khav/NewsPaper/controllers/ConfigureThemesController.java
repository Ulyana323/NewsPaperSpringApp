package ru.khav.NewsPaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.models.Themes;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.services.PersonService;

import java.util.List;

@Controller
@RestController
@RequestMapping("/config")
@ControllerAdvice
public class ConfigureThemesController {
    @Autowired
    PersonService personService;
    @Autowired
    NewsService newsService;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/addPrefer")
    public ResponseEntity<?> addPrefer(@RequestParam String theme, @RequestParam boolean status) {

        if (personService.savePrefer(theme, status) == 1) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/delPrefer")
    public ResponseEntity<?> delPrefer(@RequestParam String theme) {

        if (personService.deletePrefer(theme) == 1) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addNewTheme")
    public ResponseEntity<?> addNewTheme(@RequestParam String theme_name) throws AccessDeniedException {
        if (newsService.addNewTheme(theme_name) == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/allThemes")
    public List<String> showAllThemes()
    {
        return newsService.showAllThemes();
    }


}
