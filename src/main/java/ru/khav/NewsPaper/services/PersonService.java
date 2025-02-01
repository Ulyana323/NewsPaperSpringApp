package ru.khav.NewsPaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.Theme;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Preferences;
import ru.khav.NewsPaper.repositories.PersonRepo;
import ru.khav.NewsPaper.repositories.PreferRepo;
import ru.khav.NewsPaper.repositories.ThemeRepo;

import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {

    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private ThemeRepo themeRepo;
    @Autowired
    private PreferRepo preferRepo;

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

    @Transactional
    public int savePrefer(String theme_name,Person person,boolean status)
    {
        Preferences prefer= new Preferences(themeRepo.findByName(theme_name).get().getId(),
                person.getId(),status);
        preferRepo.save(prefer);

        person.getPreferences().add(prefer);
        themeRepo.findByName(theme_name).get().getPreferences().add(prefer);

        return 1;
    }

    @Transactional
    public int deletePrefer(String theme_name,Person person,boolean status)
    {
        Preferences pdel= preferRepo.findByUserIdAndThemeId(person.getId(),themeRepo.findByName(theme_name).get().getId()).get();
        preferRepo.delete(pdel);
        person.getPreferences().remove(pdel);
        themeRepo.findByName(theme_name).get().getPreferences().remove(pdel);

        return 1;
    }



    public Optional<Person> findByEmail(String email) {
        return personRepo.findByEmail(email);
    }
}
