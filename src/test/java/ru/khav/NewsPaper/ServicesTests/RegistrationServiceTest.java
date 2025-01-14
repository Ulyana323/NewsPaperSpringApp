package ru.khav.NewsPaper.ServicesTests;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.khav.NewsPaper.DTO.PersonRegistrationDTO;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Role;
import ru.khav.NewsPaper.security.JWTUtill;
import ru.khav.NewsPaper.services.PersonService;
import ru.khav.NewsPaper.services.RegistrationService;
import ru.khav.NewsPaper.utill.NotUniqueEmailException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JWTUtill jwtUtill;

    @Mock
    private PersonService personService;

    private PersonRegistrationDTO personRegistrationDTO;
    private Person person;

    @BeforeEach//избегаем инициализации в каждом методе
    public void setUpParamsToTestIt() {
        personRegistrationDTO = new PersonRegistrationDTO("name","lastname","email","pass");
        personRegistrationDTO.setEmail("test@example.com");
        personRegistrationDTO.setPassword("password");

        person = new Person();
        person.setEmail(personRegistrationDTO.getEmail());
        person.setPassword("encodedPassword");
        person.setRole(new Role(2, "ROLE_USER"));
    }

    @Test
    public void testRegistrSuccess() {
        // given
        when(modelMapper.map(any(), eq(Person.class))).thenReturn(person);
        when(personService.findByEmail(person.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(personRegistrationDTO.getPassword())).thenReturn("encodedPassword");
        when(jwtUtill.generateToken(person.getEmail())).thenReturn("token");

        // when
        String token = registrationService.registr(personRegistrationDTO);

        // then
        assertEquals("token", token);
        verify(personService).save(person);
        verify(bCryptPasswordEncoder).encode(personRegistrationDTO.getPassword());
    }

    @Test
    public void testRegistr_EmailAlreadyExists() {
        // given when
        when(modelMapper.map(any(), eq(Person.class))).thenReturn(person);
        when(personService.findByEmail(person.getEmail())).thenReturn(Optional.of(person));

        // then
        assertThrows(NotUniqueEmailException.class, () -> registrationService.registr(personRegistrationDTO));
        verify(personService, never()).save(any(Person.class));
    }
}
