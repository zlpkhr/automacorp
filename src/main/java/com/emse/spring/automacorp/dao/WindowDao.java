package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.entity.WindowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WindowDao extends JpaRepository<WindowEntity, Long>, WindowDaoCustom {
}
