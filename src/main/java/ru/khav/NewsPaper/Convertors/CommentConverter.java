package ru.khav.NewsPaper.Convertors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.models.Comment;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentConverter {

    private final ModelMapper modelMapper;

    public CommentConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CommentShowDTO convertToDTO(Comment comment)
    {
        return modelMapper.map(comment,CommentShowDTO.class);
    }

    public List<CommentShowDTO> convertListToDTO(List<Comment> comments)
    {
        if(comments.isEmpty())
        {
            return null;
        }else {
            List<CommentShowDTO> newComments=new ArrayList<>();
            for(Comment c:comments)
            {
                newComments.add(convertToDTO(c));
            }
            return newComments;
        }
    }
}
