package com.melksanj.web;

import com.melksanj.config.OpenAIService;
import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.model.City;
import com.melksanj.service.MelksanjService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/melksanj")
@RequiredArgsConstructor
public class MelksanjController {

    private final MelksanjService melksanjService;
    private final OpenAIService openAIService;

    /**
     * Import real estate ads data from CSV file into the database.
     */
    @PostMapping("/data/import")
    public void importCsvData() {
        melksanjService.importCsvData();
    }



    @GetMapping("/translate")
    public String translate(@RequestParam String cityName) {
        return openAIService.getNeighborhoodInfo(cityName);
    }


}
