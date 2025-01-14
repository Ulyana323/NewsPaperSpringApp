package ru.khav.NewsPaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.repositories.PersonRepo;

import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {

    @Autowired
    private PersonRepo personRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> personOptional = personRepo.findByEmail(email);

        if (!personOptional.isPresent()) {
            throw new UsernameNotFoundException("not found!");
        } else {

            return personOptional.get();
        }
    }

    @Transactional
    public void save(Person person) {
        personRepo.save(person);
    }
    public Optional<Person> findByEmail(String email) {
        return personRepo.findByEmail(email);
    }
}
