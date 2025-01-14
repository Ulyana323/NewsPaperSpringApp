package ru.khav.NewsPaper.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.DTO.CommentDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.repositories.CommentRepo;

@Service
public class CommentService {

    @Autowired
    CommentRepo commentRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AuthorizeService authorizeService;
    @Autowired
    PersonService personService;
    @Autowired
    NewsService newsService;

    @Transactional
    public String addComment(CommentDTO commentDTO, int NewsId) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && newsService.findbyId(NewsId)!=null) {
            comment.setOwner(personService.findByEmail(auth.getName()).get());
            comment.setNews(newsService.findbyId(NewsId));
            commentRepo.save(comment);
            return "yep";
        }
        return "nop";
    }


}
