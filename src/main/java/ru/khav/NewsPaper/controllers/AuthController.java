package ru.khav.NewsPaper.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.DTO.PersonAuthorizationDTO;
import ru.khav.NewsPaper.DTO.PersonRegistrationDTO;
import ru.khav.NewsPaper.config.JWTFilter;
import ru.khav.NewsPaper.models.BlackListTokens;
import ru.khav.NewsPaper.security.JWTUtill;
import ru.khav.NewsPaper.services.AuthorizeService;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.services.RegistrationService;
import ru.khav.NewsPaper.utill.ErrorResponse;
import ru.khav.NewsPaper.utill.NotUniqueEmailException;


import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/auth")
@RestController
@ControllerAdvice
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
    AuthorizeService authorizeService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/registration")
    public ResponseEntity<String> registr(@RequestBody @Valid PersonRegistrationDTO personRegistrationDTO,BindingResult bindingResult) throws NotUniqueEmailException {
        if(bindingResult.hasErrors())
        {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder str=new StringBuilder();
            for (ObjectError error : allErrors) {
                str.append(error.getDefaultMessage()).append(";");
            }
            return new ResponseEntity<>(str.toString(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(registrationService.registr(personRegistrationDTO), HttpStatus.ACCEPTED);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid PersonAuthorizationDTO personAuthorizationDTO, BindingResult bindingResult) {
        logger.info("HERE!");
        if(bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder str=new StringBuilder();
            for (ObjectError error : allErrors) {
                str.append(error.getDefaultMessage()).append(";");
            }
            return new ResponseEntity<>(str.toString(),HttpStatus.BAD_REQUEST);
        }
        logger.info("oeee");
        return new ResponseEntity<>(authorizeService.Authorize(personAuthorizationDTO),
                HttpStatus.OK);
    }

    @GetMapping("/logout")//при выходе текущий токен польз добавляется в черный список
    public ResponseEntity<HttpStatus> logout( @RequestHeader(value = "Authorization") String authorizationHeader)
    {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        jwtUtill.addTokenToBlackList(authorizationHeader);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @ExceptionHandler(NotUniqueEmailException.class)
    public ResponseEntity<ErrorResponse>exception(Exception e)
    {
        ErrorResponse response= new ErrorResponse("this email not unique",
                System.currentTimeMillis());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }



}
