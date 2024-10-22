package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.WindowDao;
import com.emse.spring.automacorp.dto.WindowDto;
import com.emse.spring.automacorp.entity.WindowEntity;
import com.emse.spring.automacorp.mapper.WindowMapper;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<WindowDto> create(@RequestBody WindowDto dto) {
        WindowEntity windowEntity = new WindowEntity(dto.name(), null, null);
        windowEntity = windowDao.save(windowEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(WindowMapper.of(windowEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WindowDto> update(@PathVariable Long id, @RequestBody WindowDto dto) {
        WindowEntity existingWindow = windowDao.findById(id).orElse(null);
        if (existingWindow == null) {
            return ResponseEntity.notFound().build();
        }
        existingWindow.setName(dto.name());
        WindowEntity updatedWindow = windowDao.save(existingWindow);
        return ResponseEntity.ok(WindowMapper.of(updatedWindow));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WindowDto> findOne(@PathVariable Long id) {
        return windowDao.findById(id).map(window -> ResponseEntity.ok(WindowMapper.of(window)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (windowDao.existsById(id)) {
            windowDao.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
