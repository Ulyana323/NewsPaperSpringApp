package ru.khav.NewsPaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khav.NewsPaper.models.Role;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}
