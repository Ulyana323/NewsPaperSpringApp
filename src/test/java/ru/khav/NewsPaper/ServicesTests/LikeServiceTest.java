package ru.khav.NewsPaper.ServicesTests;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.khav.NewsPaper.models.Like;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.repositories.LikeRepo;
import ru.khav.NewsPaper.repositories.NewsRepo;
import ru.khav.NewsPaper.services.LikeRepoCastImpl;
import ru.khav.NewsPaper.services.LikeService;
import ru.khav.NewsPaper.services.PersonService;

import java.util.Optional;

public class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepo likeRepo;

    @Mock
    private LikeRepoCastImpl likeRepoCast;

    @Mock
    private PersonService personService;

    @Mock
    private NewsRepo newsRepo;

    private Person user;
    private News news;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Person(1, "name", "lastname", "email", "1");
        news = new News("title", "text");

    }

    @Test
    public void testLike() {
        Like like = new Like(news, user);

        likeService.like(user, news);

        verify(likeRepo, times(1)).save(like);
    }

    @Test
    public void testUnlike_WhenLikeExists() {
        when(personService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(newsRepo.findByTitle(news.getTitle())).thenReturn(Optional.of(news));
        when(likeRepoCast.existsLike(user.getId(), news.getId())).thenReturn(true);

        int likeId = 1; // id лайка для удаления
        Like likeToDelete = new Like(news, user);
        likeToDelete.setId(likeId);

        when(likeRepoCast.getLikeId(user.getId(), news.getId())).thenReturn(likeId);
        when(likeRepo.findById(likeId)).thenReturn(Optional.of(likeToDelete));

        int result = likeService.unlike(news.getTitle(), user.getEmail());

        verify(likeRepo, times(1)).delete(likeToDelete);
        assertEquals(1, result);
    }

    @Test
    public void testUnlike_WhenLikeNotExist() {
        when(personService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(newsRepo.findByTitle(news.getTitle())).thenReturn(Optional.of(news));
        when(likeRepoCast.existsLike(user.getId(), news.getId())).thenReturn(false);

        int result = likeService.unlike(news.getTitle(), user.getEmail());

        verify(likeRepo, never()).delete(any());
        assertEquals(0, result);
    }
}