package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.RoomDao;
import com.emse.spring.automacorp.entity.FakeEntityBuilder;
import com.emse.spring.automacorp.entity.RoomEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomDao roomDao;

    @Test
    void shouldFindAll() throws Exception {
        RoomEntity room1 = FakeEntityBuilder.createRoomEntity(1L, "Room 1", 1);
        RoomEntity room2 = FakeEntityBuilder.createRoomEntity(2L, "Room 2", 2);

        Mockito.when(roomDao.findAll()).thenReturn(List.of(room1, room2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("[*].name").value(Matchers.containsInAnyOrder("Room 1", "Room 2")));
    }

    @Test
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(roomDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void shouldFindById() throws Exception {
        RoomEntity room = FakeEntityBuilder.createRoomEntity(1L, "Room 1", 1);
        Mockito.when(roomDao.findById(1L)).thenReturn(Optional.of(room));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Room 1"));
    }

    @Test
    void shouldUpdate() throws Exception {
        RoomEntity room = FakeEntityBuilder.createRoomEntity(1L, "Room 1", 1);
        RoomCommand roomCommand = new RoomCommand(1L, "Updated Room 1", 1, null, 22.0, List.of());
        String json = objectMapper.writeValueAsString(roomCommand);

        Mockito.when(roomDao.findById(1L)).thenReturn(Optional.of(room));
        Mockito.when(roomDao.save(Mockito.any(RoomEntity.class))).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Room 1"));
    }

    @Test
    void shouldCreate() throws Exception {
        RoomEntity room = FakeEntityBuilder.createRoomEntity(1L, "New Room", 1);
        RoomCommand roomCommand = new RoomCommand(null, "New Room", 1, null, 22.0, List.of());
        String json = objectMapper.writeValueAsString(roomCommand);

        Mockito.when(roomDao.save(Mockito.any(RoomEntity.class))).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Room"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void shouldDelete() throws Exception {
        Mockito.when(roomDao.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/rooms/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
