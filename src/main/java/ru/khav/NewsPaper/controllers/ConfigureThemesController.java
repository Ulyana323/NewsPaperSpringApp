package ru.khav.NewsPaper.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/config")
@ControllerAdvice
public class ConfigureThemesController {

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/addPrefer")
    //todo
    public ResponseEntity<Integer> addPrefer(@RequestParam String theme,) {

    }
}
