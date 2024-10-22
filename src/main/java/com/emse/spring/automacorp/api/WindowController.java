package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.WindowDao;
import com.emse.spring.automacorp.dto.WindowDto;
import com.emse.spring.automacorp.entity.WindowEntity;
import com.emse.spring.automacorp.mapper.WindowMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/windows")
public class WindowController {
    private final WindowDao windowDao;

    public WindowController(WindowDao windowDao) {
        this.windowDao = windowDao;
    }

    @GetMapping
    public List<WindowDto> findAll() {
        return windowDao.findAll().stream().map(WindowMapper::of).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WindowDto findById(@PathVariable Long id) {
        return windowDao.findById(id).map(WindowMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<WindowDto> create(@RequestBody WindowDto dto) {
        WindowEntity entity = new WindowEntity(dto.name(), null, null);
        WindowEntity saved = windowDao.save(entity);
        return ResponseEntity.ok(WindowMapper.of(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WindowDto> update(@PathVariable Long id, @RequestBody WindowCommand window) {
        WindowEntity entity = windowDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        entity.setName(window.name());
        entity.getWindowStatus().setName(window.windowStatus().name());
        entity.getWindowStatus().setValue(window.windowStatus().value());
        entity.getWindowStatus().setSensorType(window.windowStatus().sensorType());

        return ResponseEntity.ok(WindowMapper.of(entity));
    }



    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        windowDao.deleteById(id);
    }
}
