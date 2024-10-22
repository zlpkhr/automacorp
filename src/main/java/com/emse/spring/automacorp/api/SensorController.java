package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.SensorDao;
import com.emse.spring.automacorp.dto.SensorDto;
import com.emse.spring.automacorp.entity.SensorEntity;
import com.emse.spring.automacorp.mapper.SensorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/sensors")
@Transactional
public class SensorController {
    private final SensorDao sensorDao;

    public SensorController(SensorDao sensorDao) {
        this.sensorDao = sensorDao;
    }

    @GetMapping
    public List<SensorDto> findAll() {
        return sensorDao.findAll()
                .stream()
                .map(SensorMapper::of)
                .sorted(Comparator.comparing(SensorDto::name))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public SensorDto findById(@PathVariable Long id) {
        return sensorDao.findById(id).map(SensorMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<SensorDto> create(@RequestBody SensorCommand sensor) {
        SensorEntity entity = new SensorEntity(sensor.sensorType(), sensor.name());
        entity.setValue(sensor.value());
        SensorEntity saved = sensorDao.save(entity);
        return ResponseEntity.ok(SensorMapper.of(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<SensorDto> update(@PathVariable Long id, @RequestBody SensorCommand sensor) {
        SensorEntity entity = sensorDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        entity.setValue(sensor.value());
        entity.setName(sensor.name());
        entity.setSensorType(sensor.sensorType());

        return ResponseEntity.ok(SensorMapper.of(entity));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        sensorDao.deleteById(id);
    }


}

