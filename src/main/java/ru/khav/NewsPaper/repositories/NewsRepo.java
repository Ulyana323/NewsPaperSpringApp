package ru.khav.NewsPaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khav.NewsPaper.models.News;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepo extends JpaRepository<News,Integer> {

   List<News> findAll();
    Optional<List<News>> findAllByOrderByCreatedAtDesc();


  Optional<News> findByTitle(String title);
}
