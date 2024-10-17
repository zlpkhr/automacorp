package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao extends JpaRepository<RoomEntity, Long> {
}
