package ru.khav.NewsPaper.services;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Service
public class LikeRepoCastImpl {
    @PersistenceContext
    private EntityManager entityManager;

    //метод проверяет с помощью кастомного запроса есть ли лайк между определенной новостью и пользователем по их id
    public boolean existsLike(int userId, int newsId) throws RuntimeException {
        String jpql = "SELECT COUNT(l) > 0 FROM Like l WHERE l.personOwnLike.id = :userId AND l.newsOwnLike.id = :newsId";
        try {
            TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class);
            query.setParameter("userId", userId);
            query.setParameter("newsId", newsId);
            System.out.println(query.getSingleResult());
            return query.getSingleResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getLikeId(int userId, int newsId) {
        String jpql = "SELECT l.id FROM Like l WHERE l.personOwnLike.id = :userId AND l.newsOwnLike.id = :newsId";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        query.setParameter("userId", userId);
        query.setParameter("newsId", newsId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

}
