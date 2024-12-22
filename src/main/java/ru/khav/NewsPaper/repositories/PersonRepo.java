package ru.khav.NewsPaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.Person;

import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person,Integer> {

Optional<Person> findByName(String name);
}
