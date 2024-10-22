package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dto.ApiGouvAdress;
import com.emse.spring.automacorp.service.AdressSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class AdressSearchController {

    private final AdressSearchService adressSearchService;

    public AdressSearchController(AdressSearchService adressSearchService) {
        this.adressSearchService = adressSearchService;
    }

    @GetMapping("/api/adress")
    public List<ApiGouvAdress> search(@RequestParam String criteria) {
        List<String> searchTerms = Arrays.asList(criteria.split("-+"));

        return adressSearchService.searchAdress(searchTerms);
    }
}
