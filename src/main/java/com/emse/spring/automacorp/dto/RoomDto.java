package com.emse.spring.automacorp.dto;

import java.util.List;

public record RoomDto(Long id, String name, Integer floor, Double currentTemperature, Double targetTemperature,
                      List<WindowDto> windowDtos) {
}