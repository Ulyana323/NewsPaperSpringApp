package ru.khav.NewsPaper.ServicesTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.khav.NewsPaper.Convertors.CommentConverter;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentConvertorTest {

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    CommentConverter commentConverter;

    @Test
    public void testConvertToDTONullComment() {
        //given
        CommentShowDTO result = commentConverter.convertToDTO(null);
        //then
        assertNull(result);
    }

    @Test
    public void testConvertToDTOValidComment() {
        //given
        Comment comment = new Comment(1,new Date(),"text",new Person(1,"o","o","o","o",Collections.emptyList()),new News());
        CommentShowDTO expectedDto = new CommentShowDTO(comment.getText(),comment.getOwner().getName(),
                comment.getOwner().getLastname(),comment.getCreatedAt());
        when(modelMapper.map(comment, CommentShowDTO.class)).thenReturn(expectedDto);
        //when
        CommentShowDTO result = commentConverter.convertToDTO(comment);
        //then
        assertNotNull(result);
        assertEquals(expectedDto.getText(), result.getText());

    }

    @Test
    public void testConvertListToDTO_EmptyList() {
        //when
        List<CommentShowDTO> result = commentConverter.convertListToDTO(Collections.emptyList());
        //then
        assertNull(result);
    }

    @Test
    public void testConvertListToDTOValidComments() {
        //given
        Comment comment1 = new Comment(1,new Date(),"text",new Person(1,"o","o","o","o",Collections.emptyList()),new News());
        Comment comment2 = new Comment(2,new Date(),"text2",new Person(1,"o","o","o","o",Collections.emptyList()),new News());
        List<Comment> comments = new ArrayList<>(Arrays.asList(comment2,comment1));
        CommentShowDTO dto1 = new CommentShowDTO(comment1.getText(),comment1.getOwner().getName(),
                comment1.getOwner().getLastname(),comment1.getCreatedAt());
        CommentShowDTO dto2 = new CommentShowDTO(comment2.getText(),comment2.getOwner().getName(),
                comment2.getOwner().getLastname(),comment2.getCreatedAt());
        when(modelMapper.map(comment1, CommentShowDTO.class)).thenReturn(dto1);
        when(modelMapper.map(comment2, CommentShowDTO.class)).thenReturn(dto2);

        //when
        List<CommentShowDTO> result = commentConverter.convertListToDTO(comments);

        //then
        assertNotNull(result);
       assertEquals(2, result.size());
        assertEquals(dto2.getText(), result.get(0).getText());
        assertEquals(dto1.getText(), result.get(1).getText());
    }
}

