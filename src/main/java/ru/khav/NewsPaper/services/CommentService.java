package ru.khav.NewsPaper.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khav.NewsPaper.DTO.CommentDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.News;
import ru.khav.NewsPaper.models.Person;
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

    @Transactional//админ может удалять любой коммент а юзер только свой
    public int deleteComment(String title,int commentId)
    {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Person curUsr=(Person) auth.getPrincipal();
        News news=newsService.FindByTitle(title);
        if(news.getComments().stream().filter(x->x.getOwner().equals(curUsr)).filter(x->x.getId()==commentId).count()==1)
        {
            commentRepo.delete(commentRepo.findById(commentId).get());
            return 1;
        } else if (curUsr.getRole().getRoleName().equals("ROLE_ADMIN")) {
            commentRepo.delete(commentRepo.findById(commentId).get());
            return 1;
        }
        return 0;

    }



}
