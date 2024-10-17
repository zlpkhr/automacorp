package com.emse.spring.automacorp.dto;

import java.util.List;

public record Room(Long id, String name, Integer floor, Double currentTemperature, Double targetTemperature,
                   List<Window> windows) {
}