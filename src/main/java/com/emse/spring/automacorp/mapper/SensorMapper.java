package com.emse.spring.automacorp.mapper;

import com.emse.spring.automacorp.dto.SensorDto;
import com.emse.spring.automacorp.entity.SensorEntity;

public class SensorMapper {
    public static SensorDto of(SensorEntity sensorEntity) {
        return new SensorDto(
                sensorEntity.getId(),
                sensorEntity.getName(),
                sensorEntity.getValue(),
                sensorEntity.getSensorType()
        );
    }
}

