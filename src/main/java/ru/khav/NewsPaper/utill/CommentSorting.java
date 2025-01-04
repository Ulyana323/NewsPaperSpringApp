package ru.khav.NewsPaper.utill;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.khav.NewsPaper.Convertors.CommentConverter;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.Person;
import ru.khav.NewsPaper.repositories.CommentRepo;
import ru.khav.NewsPaper.services.CommentService;

import javax.persistence.Column;
import java.util.List;

@Component
public class CommentSorting extends CommentService {
    private final CommentRepo commentRepo;
    private final CommentConverter commentConverter;

    public CommentSorting(CommentRepo commentRepo, CommentConverter commentConverter) {
        this.commentRepo = commentRepo;
        this.commentConverter = commentConverter;
    }

    public List<CommentShowDTO> ShowComments(int page,String title)
    {
        if(commentRepo.findAllByNews_Title(title).size()<page*3) {
        page=0;
        }

        List<Comment> comments=commentRepo.findAllByNews_Title(title,PageRequest.of(page,3, Sort.by("createdAt").descending()));
        return commentConverter.convertListToDTO(comments);
    }
}
