package com.emse.spring.automacorp.service;

import com.emse.spring.automacorp.dto.ApiGouvAdress;
import com.emse.spring.automacorp.dto.ApiGouvFeature;
import com.emse.spring.automacorp.dto.ApiGouvResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestClientTest(AdressSearchService.class) //
class AdressSearchServiceTest {
    @Autowired
    private AdressSearchService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockRestServiceServer server; //

    @Test
    void shouldFindAdresses() throws JsonProcessingException {
        // Arrange
        ApiGouvResponse expectedResponse = simulateApiResponse();

        String expectedUrl = UriComponentsBuilder
                .fromUriString("/search")
                .queryParam("q", "cours+fauriel")
                .queryParam("limit", 15)
                .build()
                .toUriString();

        this.server
                .expect(MockRestRequestMatchers.requestTo(expectedUrl))
                .andRespond(
                        MockRestResponseCreators.withSuccess(
                                objectMapper.writeValueAsString(expectedResponse),
                                MediaType.APPLICATION_JSON
                        )
                );

        // Act
        List<ApiGouvAdress> adresses = this.service.searchAdress(List.of("cours", "fauriel"));

        // Assert
        Assertions
                .assertThat(adresses)
                .hasSize(1)
                .extracting(ApiGouvAdress::city)
                .contains("Saint Etienne");
    }

    private ApiGouvResponse simulateApiResponse() {
        ApiGouvAdress adress = new ApiGouvAdress(
                "ad1",
                "Cours Fauriel 42100 Saint-Étienne",
                "2",
                0.98,
                "42100",
                "42218",
                "Saint Etienne",
                "context",
                "type",
                0.0,
                0.0
        );

        ApiGouvFeature feature = new ApiGouvFeature("type", adress);
        return new ApiGouvResponse("v1", List.of(feature));
    }
}