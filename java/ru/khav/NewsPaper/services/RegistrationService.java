package ru.khav.NewsPaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.utill.NotUniqueNameException;

import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private PersonService personService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

@Transactional
public void registr(Person person)
{
    Optional<Person> person1=personService.findByName(person);
    if(person1.isPresent())
    {
        throw new NotUniqueNameException();
    }
    person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
personService.save(person);

}

}
