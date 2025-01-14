package ru.khav.NewsPaper.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.DTO.PersonRegistrationDTO;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Role;
import ru.khav.NewsPaper.security.JWTUtill;
import ru.khav.NewsPaper.utill.NotUniqueEmailException;

import java.util.Optional;

@Service
public class
RegistrationService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    JWTUtill jwtUtill;
    @Autowired
    private PersonService personService;

    @Transactional
    public String registr(PersonRegistrationDTO person) {
        Person personToSave = modelMapper.map(person, Person.class);
        Optional<Person> personOpt = personService.findByEmail(personToSave.getEmail());
        if (personOpt.isPresent()) {
            throw new NotUniqueEmailException();
        }
        personToSave.setRole(new Role(2,"ROLE_USER"));
        personToSave.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        personService.save(personToSave);
        return jwtUtill.generateToken(personToSave.getEmail());
    }

}
