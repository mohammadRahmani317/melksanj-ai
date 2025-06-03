package com.melksanj.web;

import com.melksanj.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/melksanj/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    /**
     * Returns the yearly price growth rate for each region of a given city.
     * This is used to display how much property prices have increased or decreased
     * in different regions of a city in a specific year.
     *
     * @param cityId The ID of the city for which regional growth rates are calculated.
     * @param groupCode (Optional) The code representing the ad group (e.g., rent, sale).
     * @param categoryCode (Optional) The code representing the ad category (e.g., apartment, villa).
     * @param year The target year for which growth data is required.
     * @return A map of region names to their growth percentages for the specified year.
     */
    @GetMapping("/growth/yearly")
    public Map<String, Map<String, String>> getYearlyPriceGrowthStats(@RequestParam Long cityId,
                                                                      @RequestParam(required = false) String groupCode,
                                                                      @RequestParam(required = false) String categoryCode,
                                                                      @RequestParam Integer year) {
        return regionService.getYearlyGrowthRateByRegion(cityId, groupCode, categoryCode, year);
    }


    /**
     * Returns the number of real estate ads per region within a specified city.
     * Useful for visualizing ad density or activity distribution across regions.
     *
     * @param cityId The ID of the city to fetch regional ad distribution data.
     * @param groupCode (Optional) The code representing the ad group (e.g., rent, sale).
     * @param categoryCode (Optional) The code representing the ad category (e.g., apartment, villa).
     * @return A map where each key is a region ID and the value contains data
     *         like region name and ad count.
     */

    @GetMapping("/distribution")
    public Map<Integer, Map<String, Object>> getRegionDistribution(@RequestParam Long cityId,
                                                                @RequestParam(required = false) String groupCode,
                                                                @RequestParam(required = false) String categoryCode) {
        return regionService.getRegionDistribution(cityId, groupCode, categoryCode);
    }
}
