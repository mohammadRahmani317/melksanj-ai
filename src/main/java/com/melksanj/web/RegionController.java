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

    @GetMapping("/growth/yearly")
    public Map<String, Map<String, String>> getYearlyPriceGrowthStats(@RequestParam Long cityId,
                                                                      @RequestParam(required = false) String groupCode,
                                                                      @RequestParam(required = false) String categoryCode,
                                                                      @RequestParam Integer year) {
        return regionService.getYearlyGrowthRateByRegion(cityId, groupCode, categoryCode, year);
    }

    @GetMapping("/distribution")
    public Map<Integer, Map<String, Object>> getRegionDistribution(@RequestParam Long cityId,
                                                                @RequestParam(required = false) String groupCode,
                                                                @RequestParam(required = false) String categoryCode) {
        return regionService.getRegionDistribution(cityId, groupCode, categoryCode);
    }
}
