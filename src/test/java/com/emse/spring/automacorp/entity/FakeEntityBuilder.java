package com.emse.spring.automacorp.entity;

import java.util.List;

public class FakeEntityBuilder {

    public static RoomEntity createRoomEntity(Long id, String name, Integer floor) {
        RoomEntity roomEntity = new RoomEntity(floor, name, 26.4);
        roomEntity.setId(id);

        roomEntity.setCurrentTemperature(createSensorEntity(id * 10 + 1L, "TemperatureSensor_" + name, SensorType.TEMPERATURE, 23.2));

        roomEntity.setWindows(List.of(
                createWindowEntity(id * 10 + 2L, "Window1_" + name, roomEntity),
                createWindowEntity(id * 10 + 3L, "Window2_" + name, roomEntity)
        ));

        return roomEntity;
    }

    public static WindowEntity createWindowEntity(Long id, String name, RoomEntity roomEntity) {
        WindowEntity windowEntity = new WindowEntity(
                name,
                createSensorEntity(id * 10 + 1L, "WindowStatusSensor_" + name, SensorType.STATUS, 0.0),
                roomEntity
        );
        windowEntity.setId(id);
        return windowEntity;
    }

    public static SensorEntity createSensorEntity(Long id, String name, SensorType type, Double value) {
        SensorEntity sensorEntity = new SensorEntity(type, name);
        sensorEntity.setId(id);
        sensorEntity.setValue(value);
        return sensorEntity;
    }
}
