package ru.khav.NewsPaper.ServicesTests;

import lombok.var;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.repositories.PersonRepo;
import ru.khav.NewsPaper.services.PersonService;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepo personRepo;
    @InjectMocks
    private PersonService personService;

    @Test
    @DisplayName("")
    void TestingFindByEmailMethod()
    {
        //given
        Person person= new Person(1,"Igor","Hovard","igors@gmail.com","123", Collections.emptyList());
        //When
        given(personRepo.findByEmail(person.getEmail()))
                .willReturn(Optional.of(person));
        var personReturned = personService.findByEmail(person.getEmail());
        //Then
        assertThat(personReturned).isNotNull();
        assertEquals(person,personReturned);

    }
    @Test
    @DisplayName("")
    void TestingFindByEmailPersonInMethod()
    {
        //given
        Person person= new Person(1,"Igor","Hovard","igors@gmail.com","123", Collections.emptyList());
        //When
        given(personRepo.findByEmail(person.getEmail()))
                .willReturn(Optional.of(person));
        var personReturned = personService.findByEmail(person);
        //Then
        assertThat(personReturned).isNotNull();
        assertEquals(Optional.of(person),personReturned);

    }

    @Test
    public void testSavePerson() {
        //given
        Person person= new Person(1,"Igor","Hovard","igors@gmail.com","123", Collections.emptyList());
        //When

        personRepo.save(person);
        //Then
        verify(personRepo, times(1)).save(person);
    }


}
