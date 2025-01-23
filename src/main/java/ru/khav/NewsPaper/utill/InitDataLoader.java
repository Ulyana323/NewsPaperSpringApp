package ru.khav.NewsPaper.utill;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.khav.NewsPaper.DTO.PersonRegistrationDTO;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.repositories.NewsRepo;
import ru.khav.NewsPaper.repositories.RoleRepo;
import ru.khav.NewsPaper.services.PersonService;
import ru.khav.NewsPaper.services.RegistrationService;
import ru.khav.NewsPaper.services.RoleService;

import java.util.Random;

@Component
public class InitDataLoader implements CommandLineRunner {

    private final NewsRepo newsRepo;
    private final RegistrationService registrationService;
    private final PersonService personService;
    private final RoleService roleService;


    public InitDataLoader(NewsRepo newsRepo, RegistrationService registrationService, PersonService personService, RoleRepo roleRepo, RoleService roleService) {
        this.newsRepo = newsRepo;
        this.registrationService = registrationService;
        this.personService = personService;
        this.roleService = roleService;
    }

    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String generateTextNews(int length) {
        StringBuilder str = new StringBuilder();
        int index;
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            index = random.nextInt(alphabet.length());
            str.append(alphabet.charAt(index));
        }
        return str.toString();
    }

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        String title;


        for (int i = 0; i < 5; i++) {
            title = "title" + random.nextInt();
            News news = new News(title, generateTextNews(300));
            newsRepo.save(news);
        }

       roleService.addRolesToDB();

        if (!personService.findByEmail("Admin@gmail.com").isPresent()) {
            registrationService.registrAdmin(new PersonRegistrationDTO(
                    "Admin",
                    "AdminRoot",
                    "Admin@gmail.com",
                    "111"
            ));
        }
        if (!personService.findByEmail("User@gmail.com").isPresent()) {
            registrationService.registr(new PersonRegistrationDTO(
                    "User",
                    "UserLastname",
                    "User@gmail.com",
                    "222"
            ));
        }


        System.out.println("бд наполнена 5 новостями и 1 админом " +
                "почта:Admin@gmail.com" + " пароль: 111" +
                " и 1 простым пользвателем почта:User@gmail.com пароль:222");
    }
}
