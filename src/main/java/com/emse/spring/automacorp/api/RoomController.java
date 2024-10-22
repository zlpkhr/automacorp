package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.RoomDao;
import com.emse.spring.automacorp.dao.SensorDao;
import com.emse.spring.automacorp.dto.RoomDto;
import com.emse.spring.automacorp.entity.RoomEntity;
import com.emse.spring.automacorp.entity.SensorEntity;
import com.emse.spring.automacorp.entity.WindowEntity;
import com.emse.spring.automacorp.mapper.RoomMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomDao roomDao;
    private final SensorDao sensorDao;

    public RoomController(RoomDao roomDao, SensorDao sensorDao) {
        this.roomDao = roomDao;
        this.sensorDao = sensorDao;
    }

    @GetMapping
    public List<RoomDto> findAll() {
        return roomDao.findAll().stream()
                .map(RoomMapper::of)
                .collect(Collectors.toList());
    }

    @GetMapping("/{room_id}")
    public RoomDto findById(@PathVariable("room_id") Long id) {
        return roomDao.findById(id).map(RoomMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<RoomDto> upsert(@RequestBody RoomCommand room) {
        Optional<RoomEntity> entity = roomDao.findById(room.id());

        RoomEntity roomEntity;
        if (entity.isPresent()) {
            roomEntity = entity.get();
            roomEntity.setName(room.name());
            roomEntity.setFloor(room.floor());
            roomEntity.setTargetTemperature(room.targetTemperature());
        } else {
            roomEntity = new RoomEntity(room.floor(), room.name(), room.targetTemperature());
            roomEntity.setId(room.id());
        }

        roomDao.save(roomEntity);

        SensorEntity currentTempSensor = new SensorEntity(room.currentTemperature().sensorType(), room.currentTemperature().name());
        currentTempSensor.setValue(room.currentTemperature().value());
        sensorDao.save(currentTempSensor);

        roomEntity.setCurrentTemperature(currentTempSensor);

        List<WindowEntity> windows = room.windows().stream().map(window -> {
            SensorEntity windowStatusSensor = new SensorEntity(window.windowStatus().sensorType(), window.windowStatus().name());
            sensorDao.save(windowStatusSensor);

            return new WindowEntity(window.name(), windowStatusSensor, roomEntity);
        }).collect(Collectors.toList());

        roomEntity.setWindows(windows);

        RoomEntity saved = roomDao.save(roomEntity);

        return ResponseEntity.ok(RoomMapper.of(saved));
    }


    @DeleteMapping("/{room_id}")
    public void deleteRoom(@PathVariable("room_id") Long id) {
        roomDao.deleteById(id);
    }

    @PutMapping("/{room_id}/openWindows")
    public ResponseEntity<Void> openWindows(@PathVariable("room_id") Long roomId) {
        Optional<RoomEntity> roomEntity = roomDao.findById(roomId);
        if (roomEntity.isPresent()) {
            roomEntity.get().getWindows().forEach(window -> window.getWindowStatus().setValue(1.0));
            roomDao.save(roomEntity.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{room_id}/closeWindows")
    public ResponseEntity<Void> closeWindows(@PathVariable("room_id") Long roomId) {
        Optional<RoomEntity> roomEntity = roomDao.findById(roomId);
        if (roomEntity.isPresent()) {
            roomEntity.get().getWindows().forEach(window -> window.getWindowStatus().setValue(0.0));
            roomDao.save(roomEntity.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
