package com.emse.spring.automacorp.dto;

public record WindowDto(Long id, String name, SensorDto windowStatus, Long roomId) {
}