package com.emse.spring.automacorp.dto;


import com.emse.spring.automacorp.entity.SensorType;

public record SensorDto(Long id, String name, Double value, SensorType sensorType) {
}
