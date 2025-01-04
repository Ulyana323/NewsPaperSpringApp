package ru.khav.NewsPaper.services;

import liquibase.pro.packaged.S;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.khav.NewsPaper.DTO.PersonAuthorizationDTO;
import ru.khav.NewsPaper.controllers.AuthController;
import ru.khav.NewsPaper.security.JWTUtill;

@Service
public class AuthorizeService {

    @Autowired
    JWTUtill jwtUtill;
    @Autowired
    AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthorizeService.class);
    public String Authorize(PersonAuthorizationDTO personAuthorizationDTO)
    {
        logger.info(personAuthorizationDTO.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(personAuthorizationDTO.getEmail(), personAuthorizationDTO.getPassword());
        try {
            Authentication authentication= authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return e.getMessage();
        }
        return jwtUtill.generateToken(personAuthorizationDTO.getEmail());
    }


}
