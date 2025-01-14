package ru.khav.NewsPaper.ServicesTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.khav.NewsPaper.Convertors.CommentConverter;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.DTO.NewsShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.models.Role;
import ru.khav.NewsPaper.repositories.NewsRepo;
import ru.khav.NewsPaper.services.NewsService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    NewsRepo newsRepo;
    @Mock
    CommentConverter commentConverter;

    @InjectMocks
    NewsService newsService;


    @Test
    void ShowNewsWithPage0() {
        //given
        News news0 = new News(1, "title0", "text", LocalDateTime.now().minusHours(25), true, Collections.emptyList());
        News news1 = new News(2, "titlefrst", "text", LocalDateTime.now(), true, Collections.emptyList());
        News news2 = new News(3, "title", "text", LocalDateTime.now(), true, Collections.emptyList());
        List<News> newsList = new ArrayList<>(Arrays.asList(news2, news1, news0));
        given(newsRepo.findAllByOrderByCreatedAtDesc())
                .willReturn(Optional.of(newsList));
        //when
        List<NewsShowDTO> res = newsService.showNews(0);
        //then
        verify(newsRepo).findAllByOrderByCreatedAtDesc();
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals("title", res.get(0).getTitle());
        assertEquals("titlefrst", res.get(1).getTitle());

    }

    @Test
    void ShowNewsWithPageNot0() {
        //given
        News news1 = new News(1, "titlefrst", "text", LocalDateTime.now(), true, Collections.emptyList());
        News news2 = new News(2, "title2", "text", LocalDateTime.now(), true, Collections.emptyList());
        News news3 = new News(3, "title3", "text", LocalDateTime.now(), true, Collections.emptyList());
        News news4 = new News(4, "title4", "text", LocalDateTime.now(), true, Collections.emptyList());
        News news5 = new News(5, "title5", "text", LocalDateTime.now(), true, Collections.emptyList());
        News news6 = new News(6, "title6", "text", LocalDateTime.now(), true, Collections.emptyList());
        News news7 = new News(7, "title7", "text", LocalDateTime.now().minusHours(25), true, Collections.emptyList());
        List<News> newsList = Arrays.asList(news3, news2, news1);
        Page<News> newsPage = new PageImpl<>(newsList, PageRequest.of(1, 3, Sort.by("createdAt").descending()), newsList.size());
        given(newsRepo.findAllByOrderByCreatedAtDesc())
                .willReturn(Optional.of(Arrays.asList(news6, news5, news4, news3, news2, news1, news7)));

        given(newsRepo.findAll(PageRequest.of(1, 3, Sort.by("createdAt").descending())))
                .willReturn(newsPage);
        //when
        List<NewsShowDTO> res = newsService.showNews(1);
        //then
        assertNotNull(res);
        assertEquals(3, res.size());
        assertEquals("title3", res.get(0).getTitle());
        assertEquals("titlefrst", res.get(2).getTitle());
    }

    @Test
    void CommentListTransformNotEmpty() {
        //given
        Comment com1 = new Comment(1, new Date(), "text", new Person(1, "o", "o", "o", "o", Collections.emptyList(), new Role(2, "ROLE_USER")), new News());
        Comment com2 = new Comment(2, new Date(), "txt", new Person(2, "p", "o", "o", "o", Collections.emptyList(), new Role(2, "ROLE_USER")), new News());
        List<Comment> comments = new ArrayList<>(Arrays.asList(com2, com1));
        given(commentConverter.convertToDTO(com1)).willReturn(new CommentShowDTO(com1.getText(), com1.getOwner().getName(),
                com1.getOwner().getLastname(), com1.getCreatedAt()));
        given(commentConverter.convertToDTO(com2)).willReturn(new CommentShowDTO(com2.getText(), com2.getOwner().getName(),
                com2.getOwner().getLastname(), com2.getCreatedAt()));
        //when
        List<CommentShowDTO> commentsToShow = newsService.CommentListTransform(comments);
        //then
        assertNotNull(commentsToShow);
        assertEquals(2, commentsToShow.size());
        assertEquals("txt", commentsToShow.get(0).getText());
    }

    @Test
    void CommentListTransormEmptyList() {
        //given
        List<Comment> comments = Collections.emptyList();
        //when
        List<CommentShowDTO> commentsToShow = newsService.CommentListTransform(comments);
        //then
        assertEquals(commentsToShow, Collections.emptyList());
    }

}
