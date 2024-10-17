package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.WindowEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WindowDaoCustomImpl implements WindowDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<WindowEntity> findRoomsWithOpenWindows(Long id) {
        String jpql = "select w from WindowEntity w inner join w.windowStatus s " +
                "where w.room.id = :id and s.value > 0.0 order by w.name";
        return em.createQuery(jpql, WindowEntity.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<WindowEntity> findAllWindowsByRoomName(String roomName) {
        String jpql = "SELECT w FROM WindowEntity w WHERE w.room.name = :roomName";
        return em.createQuery(jpql, WindowEntity.class)
                .setParameter("roomName", roomName)
                .getResultList();
    }
}
