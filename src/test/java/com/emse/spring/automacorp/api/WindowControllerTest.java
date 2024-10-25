package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.WindowDao;
import com.emse.spring.automacorp.dto.WindowDto;
import com.emse.spring.automacorp.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(WindowController.class)
class WindowControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WindowDao windowDao;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldFindAll() throws Exception {
        RoomEntity roomEntity = FakeEntityBuilder.createRoomEntity(1L, "Room 1", 1);
        Mockito.when(windowDao.findAll()).thenReturn(List.of(FakeEntityBuilder.createWindowEntity(1L, "Window 1", roomEntity), FakeEntityBuilder.createWindowEntity(2L, "Window 2", roomEntity)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/windows").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("[*].name").value(Matchers.containsInAnyOrder("Window 1", "Window 2")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(windowDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/windows/999").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldFindById() throws Exception {
        RoomEntity roomEntity = FakeEntityBuilder.createRoomEntity(1L, "Room 1", 1);
        WindowEntity windowEntity = FakeEntityBuilder.createWindowEntity(1L, "Window 1", roomEntity);
        Mockito.when(windowDao.findById(1L)).thenReturn(Optional.of(windowEntity));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/windows/1").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Window 1"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldNotUpdateUnknownEntity() throws Exception {
        WindowDto windowDto = new WindowDto(1L, "Window 1", null, null);
        String json = objectMapper.writeValueAsString(windowDto);

        Mockito.when(windowDao.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/windows/1").content(json).contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf())).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldUpdate() throws Exception {
        RoomEntity roomEntity = FakeEntityBuilder.createRoomEntity(1L, "Room 1", 1);
        SensorEntity sensorEntity = new SensorEntity(SensorType.STATUS, "Sensor 1");
        WindowEntity windowEntity = FakeEntityBuilder.createWindowEntity(1L, "Window 1", roomEntity);
        windowEntity.setWindowStatus(sensorEntity);
        SensorCommand sensorCommand = new SensorCommand("Updated Sensor", 50.0, SensorType.STATUS);
        WindowCommand command = new WindowCommand("Updated Window 1", sensorCommand);
        String json = objectMapper.writeValueAsString(command);

        Mockito.when(windowDao.findById(1L)).thenReturn(Optional.of(windowEntity));
        Mockito.when(windowDao.save(Mockito.any(WindowEntity.class))).thenReturn(windowEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/windows/1").content(json).contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Window 1")).andExpect(MockMvcResultMatchers.jsonPath("$.windowStatus.name").value("Updated Sensor")).andExpect(MockMvcResultMatchers.jsonPath("$.windowStatus.value").value(50.0)).andExpect(MockMvcResultMatchers.jsonPath("$.windowStatus.sensorType").value(SensorType.STATUS.toString()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldCreate() throws Exception {
        RoomEntity roomEntity = FakeEntityBuilder.createRoomEntity(1L, "Room 1", 1);
        WindowEntity windowEntity = FakeEntityBuilder.createWindowEntity(1L, "New Window", roomEntity);
        WindowDto windowDto = new WindowDto(null, "New Window", null, roomEntity.getId());
        String json = objectMapper.writeValueAsString(windowDto);

        Mockito.when(windowDao.save(Mockito.any(WindowEntity.class))).thenReturn(windowEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/windows").content(json).contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Window")).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldDelete() throws Exception {
        Mockito.when(windowDao.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/windows/1").with(csrf())).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
