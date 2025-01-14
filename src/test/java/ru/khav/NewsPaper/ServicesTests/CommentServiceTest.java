package ru.khav.NewsPaper.ServicesTests;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.khav.NewsPaper.DTO.CommentDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.repositories.CommentRepo;
import ru.khav.NewsPaper.services.CommentService;
import ru.khav.NewsPaper.services.NewsService;
import ru.khav.NewsPaper.services.PersonService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PersonService personService;

    @Mock
    private NewsService newsService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        // контекст безопасности для тестов
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAddComment_Success() {
        //given
        CommentDTO commentDTO = new CommentDTO();
        Comment comment = new Comment();
        Person person = new Person();
        News news = new News();
        int newsId = 1;

        given(authentication.getName()).willReturn("user@gmail.com");
        given(personService.findByEmail("user@gmail.com")).willReturn(Optional.of(person));
        given(newsService.findbyId(newsId)).willReturn(news);
        given(modelMapper.map(commentDTO, Comment.class)).willReturn(comment);

        //when
        String result = commentService.addComment(commentDTO, newsId);

        //then
        assertEquals("yep", result);
        verify(commentRepo).save(comment);
        assertEquals(person, comment.getOwner());
        assertEquals(news, comment.getNews());
    }

    @Test
    public void testAddComment_UserNotAuthenticated() {
        //given
        SecurityContextHolder.clearContext(); // Убираем аутентификацию

        CommentDTO commentDTO = new CommentDTO();
        int newsId = 1;

        //when
        String result = commentService.addComment(commentDTO, newsId);

        //then
        assertEquals("nop", result);
        verify(commentRepo, never()).save(any(Comment.class)); // Убедимся, что save не был вызван
    }
}