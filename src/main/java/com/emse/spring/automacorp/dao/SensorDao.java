package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.entity.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDao extends JpaRepository<SensorEntity, Long> {
}
