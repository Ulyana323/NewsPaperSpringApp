package ru.khav.NewsPaper.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.Person;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {
 List<Comment> findAll();

 List<Comment> findAllByNews_Title(String title, PageRequest pageRequest);

 List<Comment> findAllByNews_Title(String title);
}
