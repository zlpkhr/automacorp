package com.emse.spring.automacorp.mapper;

import com.emse.spring.automacorp.dto.SensorDto;
import com.emse.spring.automacorp.dto.WindowDto;
import com.emse.spring.automacorp.entity.FakeEntityBuilder;
import com.emse.spring.automacorp.entity.RoomEntity;
import com.emse.spring.automacorp.entity.SensorType;
import com.emse.spring.automacorp.entity.WindowEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class WindowMapperTest {

    @Test
    void shouldMapWindow() {
        RoomEntity roomEntity = FakeEntityBuilder.createRoomEntity(1L, "Room1", 1);
        WindowEntity windowEntity = FakeEntityBuilder.createWindowEntity(2L, "Window1_Room1", roomEntity);

        WindowDto windowDto = WindowMapper.of(windowEntity);

        WindowDto expectedWindowDto = new WindowDto(
                2L,
                "Window1_Room1",
                new SensorDto(21L, "WindowStatusSensor_Window1_Room1", 0.0, SensorType.STATUS),
                1L
        );

        Assertions.assertThat(windowDto).usingRecursiveComparison().isEqualTo(expectedWindowDto);
    }
}