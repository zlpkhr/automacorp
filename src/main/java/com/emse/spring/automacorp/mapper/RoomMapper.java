package com.emse.spring.automacorp.mapper;

import com.emse.spring.automacorp.dto.RoomDto;
import com.emse.spring.automacorp.dto.WindowDto;
import com.emse.spring.automacorp.entity.RoomEntity;

import java.util.List;

public class RoomMapper {
    public static RoomDto of(RoomEntity roomEntity) {
        List<WindowDto> windows = roomEntity.getWindows().stream()
                .map(WindowMapper::of)
                .toList();

        Double currentTemperatureValue = roomEntity.getCurrentTemperature() != null
                ? roomEntity.getCurrentTemperature().getValue()
                : null;

        return new RoomDto(
                roomEntity.getId(),
                roomEntity.getName(),
                roomEntity.getFloor(),
                currentTemperatureValue,
                roomEntity.getTargetTemperature(),
                windows
        );
    }
}
