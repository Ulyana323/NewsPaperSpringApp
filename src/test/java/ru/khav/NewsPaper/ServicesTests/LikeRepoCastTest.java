package ru.khav.NewsPaper.ServicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.khav.NewsPaper.services.LikeRepoCastImpl;

public class LikeRepoCastTest {

    @InjectMocks
    private LikeRepoCastImpl likeRepoCast;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Boolean> booleanQuery;

    @Mock
    private TypedQuery<Integer> integerQuery;

    @BeforeEach
    public void setUp() {//инициализирует моки
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExistsLike_ReturnsTrue_WhenLikeExists() {
        int userId = 1;
        int newsId = 1;


        when(entityManager.createQuery(anyString(), eq(Boolean.class))).thenReturn(booleanQuery);
        when(booleanQuery.setParameter("userId", userId)).thenReturn(booleanQuery);
        when(booleanQuery.setParameter("newsId", newsId)).thenReturn(booleanQuery);
        when(booleanQuery.getSingleResult()).thenReturn(true);

        boolean result = likeRepoCast.existsLike(userId, newsId);

        assertTrue(result);
        verify(entityManager).createQuery(anyString(), eq(Boolean.class));
    }

    @Test
    public void testExistsLike_ReturnsFalse_WhenLikeDoesNotExist() {
        int userId = 1;
        int newsId = 1;

        when(entityManager.createQuery(anyString(), eq(Boolean.class))).thenReturn(booleanQuery);
        when(booleanQuery.setParameter("userId", userId)).thenReturn(booleanQuery);
        when(booleanQuery.setParameter("newsId", newsId)).thenReturn(booleanQuery);
        when(booleanQuery.getSingleResult()).thenReturn(false);

        boolean result = likeRepoCast.existsLike(userId, newsId);

        assertFalse(result);
    }

    @Test
    public void testGetLikeId_ReturnsLikeId_WhenLikeExists() {
        int userId = 1;
        int newsId = 1;
        int expectedLikeId = 123;

        when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(integerQuery);
        when(integerQuery.setParameter("userId", userId)).thenReturn(integerQuery);
        when(integerQuery.setParameter("newsId", newsId)).thenReturn(integerQuery);
        when(integerQuery.getSingleResult()).thenReturn(expectedLikeId);

        int result = likeRepoCast.getLikeId(userId, newsId);

        assertEquals(expectedLikeId, result);
    }

    @Test
    public void testGetLikeId_ReturnsZero_WhenNoResult() {
        int userId = 1;
        int newsId = 1;

        when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(integerQuery);
        when(integerQuery.setParameter("userId", userId)).thenReturn(integerQuery);
        when(integerQuery.setParameter("newsId", newsId)).thenReturn(integerQuery);
        when(integerQuery.getSingleResult()).thenThrow(new NoResultException());

        int result = likeRepoCast.getLikeId(userId, newsId);

        assertEquals(0, result);
    }
}
