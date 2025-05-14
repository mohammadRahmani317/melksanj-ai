package com.melksanj.web;

import com.melksanj.config.OpenAIService;
import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.model.City;
import com.melksanj.service.MelksanjService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
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
    @GetMapping("/data/import")
    public void importCsvData() {
        melksanjService.importCsvData();
    }

    /**
     * Get average yearly prices (in billion tomans) by filters.
     *
     * @param cityId       ID of the city (required)
     * @param groupCode    Ad group code (optional)
     * @param categoryCode Ad category code (optional)
     * @return Map of year -> average price (string format)
     */
    @GetMapping("/price/yearly")
    public Map<String, String> getYearlyAveragePrices(@RequestParam Long cityId,
                                                      @RequestParam(required = false) String groupCode,
                                                      @RequestParam(required = false) String categoryCode,
                                                      @RequestParam boolean isSale) {
        return melksanjService.getYearlyAveragePrices(cityId, groupCode, categoryCode,isSale);
    }

    /**
     * Get the list of all cities.
     *
     * @return List of City
     */
    @GetMapping("/meta/cities")
    public List<City> fetchAllCities() {
         return melksanjService.fetchAllCities();
    }

    /**
     * Get the list of all ad groups.
     *
     * @return List of AdGroupDTO
     */
    @GetMapping("/meta/groups")
    public List<AdGroupDTO> fetchAllAdGroups() {
        return melksanjService.fetchAllAdGroups();
    }

    /**
     * Get the list of all ad categories.
     *
     * @return List of AdCategoryDTO
     */
    @GetMapping("/meta/categories")
    public List<AdCategoryDTO> fetchAllAdCategories() {
        return melksanjService.fetchAllAdCategories();
    }

    @GetMapping("/translate")
    public String translate(@RequestParam String cityName) {
        return openAIService.getNeighborhoodInfo(cityName);
    }
}
