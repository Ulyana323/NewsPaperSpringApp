package ru.khav.NewsPaper.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.khav.NewsPaper.DTO.PersonAuthorizationDTO;
import ru.khav.NewsPaper.DTO.PersonShowDTO;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Role;
import ru.khav.NewsPaper.security.JWTUtill;

@Service
public class AuthorizeService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeService.class);

    @Autowired
    JWTUtill jwtUtill;

    @Autowired
    AuthenticationManager authenticationManager;

    public String Authorize(PersonAuthorizationDTO personAuthorizationDTO) {
        logger.info(personAuthorizationDTO.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(personAuthorizationDTO.getEmail(), personAuthorizationDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {
            return e.getMessage();
        }

        return jwtUtill.generateToken(personAuthorizationDTO.getEmail());
    }

    public boolean IsAdmin() {
        Role role = ((Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
        if (role.getRoleName().equals("ROLE_ADMIN")) {
            return true;
        } else return false;
    }

    public PersonShowDTO showPerson() {
        Person user = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new PersonShowDTO(user.getName(), user.getLastname(), user.getEmail());
    }
}