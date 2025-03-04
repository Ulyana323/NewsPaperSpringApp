package ru.khav.NewsPaper.ServicesTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.khav.NewsPaper.DTO.PersonAuthorizationDTO;
import ru.khav.NewsPaper.security.JWTUtill;
import ru.khav.NewsPaper.services.AuthorizeService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

public class AuthorizeServiceTest {

    @Mock
    private JWTUtill jwtUtill;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthorizeService authorizeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);//чтобы моки authenticationManager не были равны null
    }

    @Test
    public void testAuthorize_Success() {
        //given
        PersonAuthorizationDTO personAuthorizationDTO = new PersonAuthorizationDTO();
        personAuthorizationDTO.setEmail("t@lala.com");
        personAuthorizationDTO.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        String token = "generated_token";

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);
        given(jwtUtill.generateToken(personAuthorizationDTO.getEmail())).willReturn(token);

        //when
        String result = authorizeService.Authorize(personAuthorizationDTO);

        //then
        assertEquals(token, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testAuthorize_BadCredentials() {
        //given
        PersonAuthorizationDTO personAuthorizationDTO = new PersonAuthorizationDTO();
        personAuthorizationDTO.setEmail("test@blabla.com");
        personAuthorizationDTO.setPassword("wrong_password");

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("Invalid credentials"));

        //when
        String result = authorizeService.Authorize(personAuthorizationDTO);

        //then
        assertEquals("Invalid credentials", result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
