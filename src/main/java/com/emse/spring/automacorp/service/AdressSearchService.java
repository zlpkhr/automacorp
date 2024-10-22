package com.emse.spring.automacorp.service;

import com.emse.spring.automacorp.dto.ApiGouvAdress;
import com.emse.spring.automacorp.dto.ApiGouvFeature;
import com.emse.spring.automacorp.dto.ApiGouvResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdressSearchService {

    private final RestTemplate restTemplate;

    public AdressSearchService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri("https://api-adresse.data.gouv.fr").build();
    }

    public List<ApiGouvAdress> searchAdress(List<String> keys) {
        String params = String.join("+", keys);

        String url = UriComponentsBuilder
                .fromUriString("/search")
                .queryParam("q", params)
                .queryParam("limit", 15)
                .build()
                .toUriString();

        ApiGouvResponse response = restTemplate.getForObject(url, ApiGouvResponse.class);

        return response.features().stream()
                .map(ApiGouvFeature::properties)
                .collect(Collectors.toList());
    }
}
