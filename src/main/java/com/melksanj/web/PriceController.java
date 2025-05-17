package com.melksanj.web;

import com.melksanj.service.MelksanjService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/melksanj/price")
@RequiredArgsConstructor
public class PriceController {

    private final MelksanjService melksanjService;

    /**
     * Get average yearly prices (in billion tomans) by filters.
     *
     * @param cityId       ID of the city (required)
     * @param groupCode    Ad group code (optional)
     * @param categoryCode Ad category code (optional)
     * @return Map of year -> average price (string format)
     */
    @GetMapping("/yearly")
    public Map<String, String> getYearlyAveragePrices(@RequestParam Long cityId,
                                                      @RequestParam(required = false) String groupCode,
                                                      @RequestParam(required = false) String categoryCode,
                                                      @RequestParam boolean isSell) {
        return melksanjService.getYearlyAveragePrices(cityId, groupCode, categoryCode, isSell);
    }

    @GetMapping("/mounthly")
    public Map<String, String> getYearlyAveragePrices(@RequestParam Long cityId,
                                                      @RequestParam Integer year,
                                                      @RequestParam(required = false) String groupCode,
                                                      @RequestParam(required = false) String categoryCode,
                                                      @RequestParam boolean isSell) {
        return melksanjService.getAveragePriceByMonthAndYear(cityId, groupCode, categoryCode, year,isSell);
    }



}
