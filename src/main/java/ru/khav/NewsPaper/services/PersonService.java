package ru.khav.NewsPaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Preferences;
import ru.khav.NewsPaper.repositories.PersonRepo;
import ru.khav.NewsPaper.repositories.PreferRepo;
import ru.khav.NewsPaper.repositories.ThemeRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public int savePrefer(String theme_name, boolean status) {
        Person curUser = findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Optional<Preferences> prefer = preferRepo.findByUserIdAndThemeId(curUser.getId(), themeRepo.findByName(theme_name).get().getId());
        if (prefer.isPresent()) {
            return 1;
        } else {
            Preferences pref = new Preferences(status,
                    curUser, themeRepo.findByName(theme_name).get());
            preferRepo.save(pref);
            curUser.getPreferences().add(pref);
            themeRepo.findByName(theme_name).get().getPreferences().add(pref);
        }
        return 1;
    }

    @Transactional
    public int deletePrefer(String theme_name) {
        Person curUser = findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Preferences pdel = preferRepo.findByUserIdAndThemeId(curUser.getId(), themeRepo.findByName(theme_name).get().getId()).get();
        preferRepo.delete(pdel);
        curUser.getPreferences().remove(pdel);
        themeRepo.findByName(theme_name).get().getPreferences().remove(pdel);

        return 1;
    }

    public Map<String, Boolean> showPrefers() {
        Person curUser = findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Map<String, Boolean> prefs = new HashMap<>();
        List<Preferences> preferences;
        if (!(preferences = preferRepo.findByUserId(curUser.getId()).get()).isEmpty()) {
            preferences.stream().forEach(x -> prefs.put(x.getTheme().getName(), x.isStatus()));
            return prefs;
        } else {
            return null;
        }
    }


    public Optional<Person> findByEmail(String email) {
        return personRepo.findByEmail(email);
    }
}
