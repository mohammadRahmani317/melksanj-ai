package com.melksanj.web;

import com.melksanj.service.MelksanjService;
import com.melksanj.service.PriceService;
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

    private final PriceService priceService;

    /**
     * Returns the average sale price per year (in billion tomans) for a specific city and optional filters.
     *
     * @param cityId       the ID of the city (required)
     * @param groupCode    optional ad group code (e.g., apartment, villa)
     * @param categoryCode optional ad category code (e.g., residential, commercial)
     * @return a map where the key is the Persian year and the value is the average price (as a formatted string)
     */
    @GetMapping("/sale/yearly")
    public Map<String, String> getYearlyAveragePrices(@RequestParam Long cityId,
                                                      @RequestParam(required = false) String groupCode,
                                                      @RequestParam(required = false) String categoryCode) {
        return priceService.getYearlyAveragePrices(cityId, groupCode, categoryCode);
    }

    /**
     * Returns the average sale price per month of a specific Persian year (in billion tomans) for a city.
     *
     * @param cityId       the ID of the city (required)
     * @param year         the Gregorian year to filter (e.g., 2024)
     * @param groupCode    optional ad group code
     * @param categoryCode optional ad category code
     * @return a map where the key is in "yyyy/MM" Persian format and the value is the average price (formatted string)
     */
    @GetMapping("/sale/monthly")
    public Map<String, String> getYearlyAveragePrices(@RequestParam Long cityId,
                                                      @RequestParam Integer year,
                                                      @RequestParam(required = false) String groupCode,
                                                      @RequestParam(required = false) String categoryCode) {
        return priceService.getAveragePriceByMonthAndYear(cityId, groupCode, categoryCode, year);
    }


}
