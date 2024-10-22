package com.emse.spring.automacorp.api;

import java.util.List;

public record RoomCommand(Long id, String name, Integer floor, SensorCommand currentTemperature,
                          Double targetTemperature, List<WindowCommand> windows) {
}
