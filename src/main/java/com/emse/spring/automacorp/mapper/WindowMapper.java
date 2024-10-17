package com.emse.spring.automacorp.mapper;

import com.emse.spring.automacorp.dto.WindowDto;
import com.emse.spring.automacorp.entity.WindowEntity;

public class WindowMapper {
    public static WindowDto of(WindowEntity windowEntity) {
        return new WindowDto(
                windowEntity.getId(),
                windowEntity.getName(),
                SensorMapper.of(windowEntity.getWindowStatus()),
                windowEntity.getRoom().getId()
        );
    }
}
