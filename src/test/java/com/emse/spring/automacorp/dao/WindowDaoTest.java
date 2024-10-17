package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.WindowEntity;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class WindowDaoTest {
    @Autowired
    private WindowDao windowDao;

    @Test
    public void shouldFindAWindowById() {
        WindowEntity window = windowDao.getReferenceById(-10L);
        Assertions.assertThat(window.getName()).isEqualTo("Window 1");
        Assertions.assertThat(window.getWindowStatus().getValue()).isEqualTo(1.0);
    }

    @Test
    public void shouldFindRoomsWithOpenWindows() {
        List<WindowEntity> result = windowDao.findRoomsWithOpenWindows(-10L);
        Assertions.assertThat(result)
                .hasSize(1)
                .extracting("id", "name")
                .containsExactly(Tuple.tuple(-10L, "Window 1"));
    }

    @Test
    public void shouldNotFindRoomsWithOpenWindows() {
        List<WindowEntity> result = windowDao.findRoomsWithOpenWindows(-9L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindWindowsByRoomName() {
        List<WindowEntity> windows = windowDao.findAllWindowsByRoomName("Room1");

        Assertions.assertThat(windows)
                .hasSize(2)
                .extracting("id", "name")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(-10L, "Window 1"),
                        Tuple.tuple(-9L, "Window 2")
                );
    }

    @Test
    public void shouldNotFindWindowsByNonExistentRoomName() {
        List<WindowEntity> windows = windowDao.findAllWindowsByRoomName("Room3");

        Assertions.assertThat(windows).isEmpty();
    }
}