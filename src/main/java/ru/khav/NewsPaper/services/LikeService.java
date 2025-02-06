package ru.khav.NewsPaper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.models.Like;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.repositories.LikeRepo;
import ru.khav.NewsPaper.repositories.NewsRepo;

@Service
public class LikeService {

    @Autowired
    LikeRepo likeRepo;
    @Autowired
    LikeRepoCastImpl likeRepoCast;
    @Autowired
    PersonService personService;
    @Autowired
    NewsRepo newsRepo;

    @Transactional
    public void like(Person user, News news) {
        if (isNewsLikesByCurrUser(user, news)) return;
        likeRepo.save(new Like(news, user));
    }

    @Transactional
    public int unlike(String titleNews, String userEmail) {
        Person curUser = personService.findByEmail(userEmail).get();
        News curNews = newsRepo.findByTitle(titleNews).get();
        if (isNewsLikesByCurrUser(curUser, curNews)) {
            Like likeToDel = likeRepo.findById(likeRepoCast.getLikeId(curUser.getId(), curNews.getId())).get();
            likeRepo.delete(likeToDel);
            curNews.getLikes().remove(likeToDel);
            curUser.getLikes().remove(likeToDel);
            System.out.println("deleted like");
            return 1;
        }
        return 0;
    }

    public boolean isNewsLikesByCurrUser(Person person, News news) {
        return likeRepoCast.existsLike(person.getId(), news.getId());
    }
}
