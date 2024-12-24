package ru.khav.NewsPaper.services;

import liquibase.pro.packaged.S;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.DTO.PersonRegistrationDTO;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.security.JWTUtill;
import ru.khav.NewsPaper.utill.NotUniqueEmailException;


import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private PersonService personService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    JWTUtill jwtUtill;

@Transactional
public String registr(PersonRegistrationDTO person)
{
    Person personToSave=modelMapper.map(person, Person.class);
    Optional<Person> personOpt=personService.findByEmail(personToSave);
    if(personOpt.isPresent())
    {
        throw new NotUniqueEmailException();
    }
    person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
personService.save(personToSave);
return jwtUtill.generateToken(personToSave.getEmail());

}

}
