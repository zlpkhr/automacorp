package com.emse.spring.automacorp.mapper;

import com.emse.spring.automacorp.dto.SensorDto;
import com.emse.spring.automacorp.entity.FakeEntityBuilder;
import com.emse.spring.automacorp.entity.SensorEntity;
import com.emse.spring.automacorp.entity.SensorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SensorMapperTest {

    @Test
    void shouldMapSensor() {
        SensorEntity sensorEntity = FakeEntityBuilder.createSensorEntity(1L, "TemperatureSensor", SensorType.TEMPERATURE, 23.5);

        SensorDto sensorDto = SensorMapper.of(sensorEntity);

        SensorDto expectedSensorDto = new SensorDto(
                1L,
                "TemperatureSensor",
                23.5,
                SensorType.TEMPERATURE
        );

        Assertions.assertThat(sensorDto).usingRecursiveComparison().isEqualTo(expectedSensorDto);
    }
}