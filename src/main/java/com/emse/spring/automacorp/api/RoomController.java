package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.RoomDao;
import com.emse.spring.automacorp.dto.RoomDto;
import com.emse.spring.automacorp.entity.RoomEntity;
import com.emse.spring.automacorp.entity.SensorEntity;
import com.emse.spring.automacorp.entity.SensorType;
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

    public RoomController(RoomDao roomDao) {
        this.roomDao = roomDao;
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

        if (entity.isPresent()) {
            entity.get().setCurrentTemperature(new SensorEntity(room.currentTemperature().sensorType(), room.currentTemperature().name()));
            entity.get().getCurrentTemperature().setValue(room.currentTemperature().value());
            entity.get().setWindows(room.windows().stream().map(window -> new WindowEntity(window.name(), new SensorEntity(window.windowStatus().sensorType(), window.windowStatus().name()), entity.get())).collect(Collectors.toList()));
            RoomEntity saved = roomDao.save(entity.get());

            return ResponseEntity.ok(RoomMapper.of(saved));
        }
        RoomEntity createdEntity = new RoomEntity(room.floor(), room.name(), room.targetTemperature());
        createdEntity.setCurrentTemperature(new SensorEntity(room.currentTemperature().sensorType(), room.currentTemperature().name()));
        createdEntity.getCurrentTemperature().setValue(room.currentTemperature().value());
        createdEntity.setWindows(room.windows().stream().map(window -> new WindowEntity(window.name(), new SensorEntity(window.windowStatus().sensorType(), window.windowStatus().name()), createdEntity)).collect(Collectors.toList()));
        RoomEntity saved = roomDao.save(createdEntity);

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
            if (roomEntity.get().getCurrentTemperature().getSensorType() != SensorType.STATUS) {
                return ResponseEntity.badRequest().build();
            }

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
            if (roomEntity.get().getCurrentTemperature().getSensorType() != SensorType.STATUS) {
                return ResponseEntity.badRequest().build();
            }
            roomEntity.get().getWindows().forEach(window -> window.getWindowStatus().setValue(0.0));
            roomDao.save(roomEntity.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
