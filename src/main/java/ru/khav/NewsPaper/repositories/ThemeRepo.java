package ru.khav.NewsPaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khav.NewsPaper.models.Themes;

import java.util.Optional;

@Repository
public interface ThemeRepo extends JpaRepository<Themes, Integer> {
    Optional<Themes> findByName(String string);


}

