package ru.khav.NewsPaper.ServicesTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.khav.NewsPaper.Convertors.CommentConverter;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.DTO.NewsDTO;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.*;
import ru.khav.NewsPaper.repositories.NewsRepo;
import ru.khav.NewsPaper.repositories.PreferRepo;
import ru.khav.NewsPaper.repositories.ThemeRepo;
import ru.khav.NewsPaper.services.LikeService;
import ru.khav.NewsPaper.services.NewsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepo newsRepo;

    @Mock
    private PreferRepo preferRepo;

    @Mock
    private CommentConverter commentConverter;
    @Mock
    ModelMapper modelMapper;

    @Mock
    private LikeService likeService;

    @Mock
    ThemeRepo themeRepo;

    private Person user;
    private List<News> newsList;

    private News news1;
    private News news2;
    private NewsDTO newsDTO;


    @BeforeEach
    public void setUp() {
        modelMapper = mock(ModelMapper.class);
        commentConverter = new CommentConverter(modelMapper);
        MockitoAnnotations.openMocks(this);
        user = new Person(1, "name", "lastname", "email", "1");

        newsList = new ArrayList<>();
       news1 =new News("title1", "text");
       news1.setId(1);
        LocalDate dat = LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth() ,LocalDate.now().getDayOfMonth());
        LocalTime tim = LocalTime.now();
        LocalDateTime ldat = LocalDateTime.of(dat, tim);
        System.out.println(ldat);
        news1.setCreatedAt(ldat);
        news1.getThemes().add(new Themes("Sports"));
        newsList.add(news1);

         news2 = new News("title2", "text");
         news2.setId(2);
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalTime time = LocalTime.now();
        LocalDateTime ldate = LocalDateTime.of(date, time);
        news2.setCreatedAt(ldate);
        newsList.add(news2);

        Set<String> th=new HashSet<>();
        th.add("Sports");
        newsDTO=new NewsDTO(news1.getId(),"New title",news1.getText(),null,th);

    }

    @Test
    public void testShowNews() {
        when(newsRepo.findAllByOrderByCreatedAtDesc()).thenReturn(Optional.of(newsList));

        List<NewsShowDTO> result = newsService.showNews(0);

        assertNotNull(result);
        assertEquals(1, result.size()); // Должен вернуть только одну новость (последние 24 часа)
        assertEquals("title1", result.get(0).getTitle());
    }

    @Test
    public void testConvertListThemes() {
        Set<Themes> themes = new HashSet<>();
        Themes theme1 = new Themes();
        theme1.setName("Sports");
        themes.add(theme1);

        Set<String> result = newsService.convertListThemes(themes);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("Sports"));
    }
    @Test
    public void testShowNewsAndLikeit() {
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(newsRepo.findByTitle("title1")).thenReturn(Optional.of(news1));

        int result = newsService.showNewsAndLikeit("title1");

        assertEquals(1, result);
        verify(likeService,times(1)).like(user, news1);

    }

    @Test
    public void testShowNewsAndLikeit_NewsNotFound() {
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(newsRepo.findByTitle("Nonexistent News")).thenReturn(Optional.empty());

        int result = newsService.showNewsAndLikeit("Nonexistent News");

        assertEquals(0, result);
    }

    @Test
    public void testCommentListTransform() {
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setOwner(user); //у комментария есть владелец
        comments.add(comment);

        when(commentConverter.convertToDTO(any(Comment.class))).thenReturn(new CommentShowDTO());

        List<CommentShowDTO> result = newsService.CommentListTransform(comments);

        assertNotNull(result);
        assertEquals(1, result.size());
    }


    @Test
    public void testEditNews() {
        when(newsRepo.findById(1)).thenReturn(Optional.of(news1));

        int result = newsService.editNews(newsDTO);

        assertEquals(1, result);assertEquals("New title", news1.getTitle());
    }

    @Test
    public void testDeleteNews() {
        when(newsRepo.findByTitle("title1")).thenReturn(Optional.of(news1));

        int result = newsService.deleteNews("title1");

        assertEquals(1, result);
        verify(newsRepo).delete(news1);
    }

    @Test
    public void testDeleteNews_NewsNotFound() {
        when(newsRepo.findByTitle("Nonexistent News")).thenReturn(Optional.empty());

        int result = newsService.deleteNews("Nonexistent News");

        assertEquals(-1, result);
    }

    @Test
    public void testAddNewTheme() {
        String themeName = "New Theme";
        when(themeRepo.findByName(themeName)).thenReturn(Optional.empty());

        int result = newsService.addNewTheme(themeName);

        assertEquals(1, result);
        verify(themeRepo).save(any(Themes.class));
    }

    @Test
    public void testAddNewTheme_ThemeExists() {
        String themeName = "Existing Theme";
        when(themeRepo.findByName(themeName)).thenReturn(Optional.of(new Themes(themeName)));

        int result = newsService.addNewTheme(themeName);

        assertEquals(0, result);
    }
}

