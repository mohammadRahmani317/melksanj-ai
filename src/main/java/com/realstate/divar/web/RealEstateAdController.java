package com.realstate.divar.web;

import com.realstate.divar.service.RealEstateAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/rest/reale/state")
@RequiredArgsConstructor
public class RealEstateAdController {
    private final RealEstateAdService realEstateAdService;

    @PostMapping("/fill/data")
    public void fillAllDataFromCsvFile(){
        realEstateAdService.loadCsvFiles();
    }

    @GetMapping("/chart/image/{city}")
    public Map<String, Double> getRealEstateAdControllerChartImageByCityPerYear(@PathVariable String city) throws IOException {
        return realEstateAdService.generatePriceTrendChart(city);
    }

}
