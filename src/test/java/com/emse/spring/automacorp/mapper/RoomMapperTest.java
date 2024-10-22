package com.emse.spring.automacorp.mapper;

import com.emse.spring.automacorp.dto.RoomDto;
import com.emse.spring.automacorp.dto.SensorDto;
import com.emse.spring.automacorp.dto.WindowDto;
import com.emse.spring.automacorp.entity.FakeEntityBuilder;
import com.emse.spring.automacorp.entity.RoomEntity;
import com.emse.spring.automacorp.entity.SensorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RoomMapperTest {

    @Test
    void shouldMapRoom() {
        RoomEntity roomEntity = FakeEntityBuilder.createRoomEntity(1L, "Room1", 1);

        RoomDto roomDto = RoomMapper.of(roomEntity);

        RoomDto expectedRoomDto = new RoomDto(
                1L,
                "Room1",
                1,
                23.2,
                26.4,
                List.of(
                        new WindowDto(
                                12L,
                                "Window1_Room1",
                                new SensorDto(121L, "WindowStatusSensor_Window1_Room1", 0.0, SensorType.STATUS),
                                1L
                        ),
                        new WindowDto(
                                13L,
                                "Window2_Room1",
                                new SensorDto(131L, "WindowStatusSensor_Window2_Room1", 0.0, SensorType.STATUS),
                                1L
                        )
                )
        );

        Assertions.assertThat(roomDto).usingRecursiveComparison().isEqualTo(expectedRoomDto);
    }
}
