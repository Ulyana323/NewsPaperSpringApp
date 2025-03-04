package ru.khav.NewsPaper.ServicesTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Preferences;
import ru.khav.NewsPaper.models.Themes;
import ru.khav.NewsPaper.repositories.PersonRepo;
import ru.khav.NewsPaper.repositories.PreferRepo;
import ru.khav.NewsPaper.repositories.ThemeRepo;
import ru.khav.NewsPaper.services.PersonService;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepo personRepo;

    @Mock
    private ThemeRepo themeRepo;

    @Mock
    private PreferRepo preferRepo;

    @InjectMocks
    private PersonService personService;

    private Person testPerson;

    @BeforeEach//чтобы не инициализировать каждый раз
    public void setUp() {
        testPerson = new Person();
        testPerson.setEmail("test@gmail.com");
        testPerson.setPassword("password");
        testPerson.setId(1);
        Authentication auth = new UsernamePasswordAuthenticationToken("test@gmail.com", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        when(personRepo.findByEmail("test@gmail.com")).thenReturn(Optional.of(testPerson));

        UserDetails userDetails = personService.loadUserByUsername("test@gmail.com");

        assertNotNull(userDetails);
        assertEquals(testPerson.getEmail(), userDetails.getUsername());
        verify(personRepo).findByEmail("test@gmail.com");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(personRepo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            personService.loadUserByUsername("notfound@example.com");
        });
    }

    @Test
    public void testSave() {
        personService.save(testPerson);
        verify(personRepo).save(testPerson);
    }


    @Test
    public void testSavePrefer_NewPreference() {
        // Настройка мока для текущего пользователя
        when(personRepo.findByEmail("test@gmail.com")).thenReturn(Optional.of(testPerson));
        when(themeRepo.findByName("theme1")).thenReturn(Optional.of(new Themes("theme1")));

        int result = personService.savePrefer("theme1", true);

        assertEquals(1, result);
        verify(preferRepo).save(any(Preferences.class));
    }

    @Test
    public void testDeletePrefer() {
        // Настройка пользователя
        when(personRepo.findByEmail("test@gmail.com")).thenReturn(Optional.of(testPerson));

        Themes testTheme = new Themes(1, "theme1");
        when(themeRepo.findByName("theme1")).thenReturn(Optional.of(testTheme));

        Preferences pref = new Preferences();
        pref.setId(1);
        pref.setUser(testPerson);
        pref.setTheme(testTheme);
        when(preferRepo.findByUserIdAndThemeId(testPerson.getId(), testTheme.getId())).thenReturn(Optional.of(pref));

        int result = personService.deletePrefer("theme1");

        assertEquals(1, result);
        verify(preferRepo).delete(pref);
        assertFalse(testPerson.getPreferences().contains(pref));
        assertFalse(testTheme.getPreferences().contains(pref));
    }
}
