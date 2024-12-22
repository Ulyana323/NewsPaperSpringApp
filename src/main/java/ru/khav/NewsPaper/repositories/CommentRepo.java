package ru.khav.NewsPaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.khav.NewsPaper.models.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {

}
