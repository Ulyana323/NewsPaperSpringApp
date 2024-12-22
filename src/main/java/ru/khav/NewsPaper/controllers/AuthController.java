package ru.khav.NewsPaper.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.DTO.PersonDTO;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.security.JWTUtill;
import ru.khav.NewsPaper.services.RegistrationService;
import ru.khav.NewsPaper.utill.ErrorResponse;
import ru.khav.NewsPaper.utill.NotUniqueNameException;
import ru.khav.NewsPaper.utill.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JWTUtill jwtUtill;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PersonValidator personValidator;

    @PostMapping("/registration")
    public ResponseEntity<String> create(@RequestBody @Valid PersonDTO personDTO,BindingResult bindingResult) throws NotUniqueNameException {
        personValidator.validate(personDTO,bindingResult);
        if(bindingResult.hasErrors())
        {
            return new ResponseEntity<>("incorrect input data",HttpStatus.BAD_REQUEST);
        }
        registrationService.registr(modelMapper.map(personDTO, Person.class));
        String token = jwtUtill.generateToken(personDTO.getName());

        return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {

        personValidator.validate(personDTO,bindingResult);
        if(bindingResult.hasErrors())
        {
            return new ResponseEntity<>("incorrect input data",HttpStatus.BAD_REQUEST);
        }


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(personDTO.getName(), personDTO.getPassword());

        try {
           Authentication authentication= authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String tokenLogged = jwtUtill.generateToken(personDTO.getName());
        return new ResponseEntity<>(tokenLogged, HttpStatus.ACCEPTED);

    }

    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout()
    {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse>exception(NotUniqueNameException e)
    {
        ErrorResponse response= new ErrorResponse("this name not unique",
                System.currentTimeMillis());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }



}
