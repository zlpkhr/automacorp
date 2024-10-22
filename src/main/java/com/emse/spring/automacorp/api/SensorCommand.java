package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.entity.SensorType;

public record SensorCommand(String name, Double value, SensorType sensorType) {
}
