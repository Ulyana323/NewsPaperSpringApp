package ru.khav.NewsPaper.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.khav.NewsPaper.DTO.PersonAuthorizationDTO;
import ru.khav.NewsPaper.DTO.PersonRegistrationDTO;
import ru.khav.NewsPaper.DTO.PersonShowDTO;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.security.JWTUtill;
import ru.khav.NewsPaper.services.AuthorizeService;
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

    @PreAuthorize("hasAuthority('ROLE_ANONYMOUS')")
    @PostMapping("/registration")
    public ResponseEntity<String> registr(@RequestBody @Valid PersonRegistrationDTO personRegistrationDTO, BindingResult bindingResult) throws NotUniqueEmailException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder str = new StringBuilder();
            for (ObjectError error : allErrors) {
                str.append(error.getDefaultMessage()).append(";");
            }
            return new ResponseEntity<>(str.toString(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(registrationService.registr(personRegistrationDTO), HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasAuthority('ROLE_ANONYMOUS')")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid PersonAuthorizationDTO personAuthorizationDTO, BindingResult bindingResult) throws AccessDeniedException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder str = new StringBuilder();
            for (ObjectError error : allErrors) {
                str.append(error.getDefaultMessage()).append(";");
            }
            return new ResponseEntity<>(str.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authorizeService.Authorize(personAuthorizationDTO),
                HttpStatus.OK);
    }



    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/logout")//при выходе текущий токен польз добавляется в черный список
    public ResponseEntity<HttpStatus> logout(@RequestHeader(value = "Authorization") String authorizationHeader) throws AccessDeniedException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        jwtUtill.addTokenToBlackList(authorizationHeader);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/getFIO")
    public ResponseEntity<PersonShowDTO> getFIO() throws AccessDeniedException {
        return new ResponseEntity<>(authorizeService.showPerson(),HttpStatus.OK);
    }

    @ExceptionHandler({NotUniqueEmailException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
