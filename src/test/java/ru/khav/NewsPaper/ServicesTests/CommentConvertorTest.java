package ru.khav.NewsPaper.ServicesTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ru.khav.NewsPaper.Convertors.CommentConverter;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentConvertorTest {

    News news = new News("title", "text");
    Person user = new Person(1, "name", "lastname", "email", "1");
    Comment testComment = new Comment(1, new Date(), "ahahah", user, news);
    private ModelMapper modelMapper;
    private CommentConverter commentConverter;

    @BeforeEach
    public void setUp() {
        modelMapper = mock(ModelMapper.class);
        commentConverter = new CommentConverter(modelMapper);

    }

    @Test
    public void testConvertToDTO_NullComment() {
        CommentShowDTO result = commentConverter.convertToDTO(null);
        assertNull(result);
    }

    @Test
    public void testConvertToDTO_ValidComment() {

        CommentShowDTO expectedDto = new CommentShowDTO(testComment.getId(), testComment.getText(), testComment.getOwner().getName(),
                testComment.getOwner().getLastname(), testComment.getCreatedAt());

        when(modelMapper.map(testComment, CommentShowDTO.class)).thenReturn(expectedDto);

        CommentShowDTO result = commentConverter.convertToDTO(testComment);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(modelMapper).map(testComment, CommentShowDTO.class);
    }

    @Test
    public void testConvertListToDTO_EmptyList() {
        List<CommentShowDTO> result = commentConverter.convertListToDTO(Collections.emptyList());
        assertNull(result);
    }

    @Test
    public void testConvertListToDTO_ValidList() {
        Comment testComment2 = new Comment(2, new Date(), "ahahah", user, news);


        CommentShowDTO dto1 = new CommentShowDTO(testComment.getId(), testComment.getText(), testComment.getOwner().getName(),
                testComment.getOwner().getLastname(), testComment.getCreatedAt());
        CommentShowDTO dto2 = new CommentShowDTO(testComment2.getId(), testComment2.getText(), testComment2.getOwner().getName(),
                testComment2.getOwner().getLastname(), testComment2.getCreatedAt());


        when(modelMapper.map(testComment, CommentShowDTO.class)).thenReturn(dto1);
        when(modelMapper.map(testComment2, CommentShowDTO.class)).thenReturn(dto2);

        List<Comment> comments = Arrays.asList(testComment, testComment2);

        List<CommentShowDTO> result = commentConverter.convertListToDTO(comments);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(modelMapper).map(testComment, CommentShowDTO.class);
        verify(modelMapper).map(testComment2, CommentShowDTO.class);
    }
}
