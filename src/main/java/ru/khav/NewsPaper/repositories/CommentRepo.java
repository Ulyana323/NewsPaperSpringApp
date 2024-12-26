package ru.khav.NewsPaper.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.khav.NewsPaper.models.Comment;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {
 List<Comment> findAll();
}
