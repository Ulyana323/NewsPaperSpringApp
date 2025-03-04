package ru.khav.NewsPaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khav.NewsPaper.models.Preferences;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferRepo extends JpaRepository<Preferences, Integer> {

    Optional<List<Preferences>> findByUserId(Integer integer);
    Optional<Preferences> findByUserIdAndThemeId(Integer integer,Integer intege);

}