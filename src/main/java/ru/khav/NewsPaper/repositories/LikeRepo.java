package ru.khav.NewsPaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khav.NewsPaper.models.Like;

@Repository
public interface LikeRepo extends JpaRepository<Like, Integer> {

}
