package ru.khav.NewsPaper.services;

import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khav.NewsPaper.models.Role;
import ru.khav.NewsPaper.repositories.RoleRepo;

@Service
public class RoleService {

    @Autowired
    RoleRepo roleRepo;

    public void addRolesToDB()
    {
        if(!roleRepo.findByRoleName("ROLE_ADMIN").isPresent())
        {
            roleRepo.save(new Role(1,"ROLE_ADMIN"));
        }
        if(!roleRepo.findByRoleName("ROLE_USER").isPresent())
        {
            roleRepo.save(new Role(2,"ROLE_USER"));
        }
    }
}
