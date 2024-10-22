package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.SensorDao;
import com.emse.spring.automacorp.entity.FakeEntityBuilder;
import com.emse.spring.automacorp.entity.SensorEntity;
import com.emse.spring.automacorp.entity.SensorType;
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


@WebMvcTest(SensorController.class)
class SensorControllerTest {
    // Spring object to mock call to our app
    @Autowired
    private MockMvc mockMvc;

    // The serializer used by Spring to send and receive data to/from the REST controller
    @Autowired
    private ObjectMapper objectMapper;

    // We choose to mock the DAO used in the REST controller to limit the scope of our test
    @MockBean
    private SensorDao sensorDao;

    @Test
    void shouldFindAll() throws Exception {
        Mockito.when(sensorDao.findAll()).thenReturn(List.of(
                FakeEntityBuilder.createSensorEntity(1L, "Temperature room 1", SensorType.TEMPERATURE, 23.5),
                FakeEntityBuilder.createSensorEntity(2L, "Temperature room 2", SensorType.TEMPERATURE, 23.5)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sensors").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("[*].name")
                                .value(Matchers.containsInAnyOrder("Temperature room 1", "Temperature room 2"))
                );
    }

    @Test
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(sensorDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sensors/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void shouldFindById() throws Exception {
        SensorEntity sensorEntity = FakeEntityBuilder.createSensorEntity(1L, "Temperature room 1", SensorType.TEMPERATURE, 23.5);
        Mockito.when(sensorDao.findById(999L)).thenReturn(Optional.of(sensorEntity));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sensors/999").accept(MediaType.APPLICATION_JSON))
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // the content can be tested with Json path
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Temperature room 1"));
    }

    @Test
    void shouldNotUpdateUnknownEntity() throws Exception {
        SensorEntity sensorEntity = FakeEntityBuilder.createSensorEntity(1L, "Temperature room 1", SensorType.TEMPERATURE, 23.5);
        SensorCommand expectedSensor = new SensorCommand(sensorEntity.getName(), sensorEntity.getValue(), sensorEntity.getSensorType());
        String json = objectMapper.writeValueAsString(expectedSensor);

        Mockito.when(sensorDao.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/sensors/1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldUpdate() throws Exception {
        SensorEntity sensorEntity = FakeEntityBuilder.createSensorEntity(1L, "Temperature room 1", SensorType.TEMPERATURE, 23.5);
        SensorCommand expectedSensor = new SensorCommand(sensorEntity.getName(), sensorEntity.getValue(), sensorEntity.getSensorType());
        String json = objectMapper.writeValueAsString(expectedSensor);

        Mockito.when(sensorDao.findById(1L)).thenReturn(Optional.of(sensorEntity));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/sensors/1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Temperature room 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    void shouldCreate() throws Exception {
        SensorEntity sensorEntity = FakeEntityBuilder.createSensorEntity(1L, "Temperature room 1", SensorType.TEMPERATURE, 23.5);
        SensorCommand expectedSensor = new SensorCommand(sensorEntity.getName(), sensorEntity.getValue(), sensorEntity.getSensorType());
        String json = objectMapper.writeValueAsString(expectedSensor);

        Mockito.when(sensorDao.existsById(1L)).thenReturn(false);
        Mockito.when(sensorDao.save(Mockito.any(SensorEntity.class))).thenReturn(sensorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/sensors")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                // check the HTTP response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Temperature room 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/sensors/999"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}