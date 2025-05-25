package com.melksanj.web;

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
    public Map<String, String> getYearlyAverageSalePrices(@RequestParam Long cityId,
                                                          @RequestParam(required = false) String groupCode,
                                                          @RequestParam(required = false) String categoryCode) {
        return priceService.getYearlyAverageSalePrices(cityId, groupCode, categoryCode);
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
    public Map<String, String> getMonthlyAverageSalePrices(@RequestParam Long cityId,
                                                           @RequestParam Integer year,
                                                           @RequestParam(required = false) String groupCode,
                                                           @RequestParam(required = false) String categoryCode) {
        return priceService.getMonthlyAverageSalePrices(cityId, groupCode, categoryCode, year);
    }


    /**
     * Returns the average rent (اجاره) and deposit (رهن) values per Persian year for a given city.
     *
     * The result includes two separate series: one for rent values (in million tomans)
     * and one for deposit values (in billion tomans). Each year is returned as a string key.
     *
     * @param cityId       the ID of the city (required)
     * @param groupCode    optional ad group code (e.g., apartment, villa)
     * @param categoryCode optional ad category code (e.g., residential, commercial)
     * @return a map where each key is the Persian year, and each value is another map containing:
     *         - "rentValue": average monthly rent (in million tomans)
     *         - "creditValue": average deposit (in billion tomans)
     */
    @GetMapping("/rent/yearly")
    public Map<String, Map<String, Long>> getYearlyRentStats(@RequestParam Long cityId,
                                                             @RequestParam(required = false) String groupCode,
                                                             @RequestParam(required = false) String categoryCode) {
        return priceService.getYearlyRentStats(cityId, groupCode, categoryCode);
    }

    /**
     * Returns the average rent and deposit values per month of a specific Persian year for a city.
     *
     * This is useful for generating monthly trend charts. The result includes monthly values
     * for both rent and deposit. The keys in the returned map follow the "yyyy/MM" Persian format.
     *
     * @param cityId       the ID of the city (required)
     * @param year         the Persian year (e.g., 1402)
     * @param groupCode    optional ad group code
     * @param categoryCode optional ad category code
     * @return a map where each key is a "yyyy/MM" string, and each value is another map containing:
     *         - "rentValue": average monthly rent (in million tomans)
     *         - "creditValue": average deposit (in billion tomans)
     */
    @GetMapping("/rent/monthly")
    public Map<String, Map<String, Long>> getMonthlyRentStats(@RequestParam Long cityId,
                                                              @RequestParam Integer year,
                                                              @RequestParam(required = false) String groupCode,
                                                              @RequestParam(required = false) String categoryCode) {
        return priceService.getMonthlyRentStats(cityId, year, groupCode, categoryCode);
    }

    /**
     * Returns the average sale price **per square meter** (in billion tomans) for each year,
     * based on a specific city and optional filters like ad group or category.
     *
     * <p>This endpoint calculates the mean of (price / buildingSize) for all matching real estate ads,
     * grouped by Persian year.</p>
     *
     * <p>Returned values are formatted as strings like "32.6" representing 32.6 billion tomans per m².</p>
     *
     * @param cityId        the ID of the city (required)
     * @param groupCode     optional ad group code (e.g., residential, commercial)
     * @param categoryCode  optional ad category code (e.g., apartment, villa)
     * @return a map where the key is the Persian year (e.g., "1400")
     *         and the value is the average price per square meter (formatted as string, in billion tomans)
     *
     * @example GET /api/melksanj/price/sale/m2/yearly?cityId=1&groupCode=residential&categoryCode=apartment
     */
    @GetMapping("/sale/m2/yearly")
    public Map<String, String> getYearlyAveragePricePerSquareMeter(@RequestParam Long cityId,
                                                                   @RequestParam(required = false) String groupCode,
                                                                   @RequestParam(required = false) String categoryCode) {
        return priceService.getYearlyAveragePricePerSquareMeter(cityId, groupCode, categoryCode);
    }


    /**
     * Returns the average sale price **per square meter per month** (in billion tomans) for a given Persian year
     * and a specific city, optionally filtered by ad group and category.
     *
     * <p>This endpoint calculates the average of (price / buildingSize) per month,
     * for real estate ads posted in the specified Gregorian year.</p>
     *
     * <p>Returned values are formatted strings like "28.4", representing billion tomans per m².</p>
     *
     * @param cityId        the ID of the city (required)
     * @param year          the Gregorian year (e.g., 2024) used to extract ads in that year
     * @param groupCode     optional ad group code (e.g., residential)
     * @param categoryCode  optional ad category code (e.g., apartment)
     * @return a map where the key is in "yyyy/MM" (Persian calendar format),
     *         and the value is the average price per square meter (as a formatted string)
     *
     * @example GET /api/melksanj/price/sale/m2/monthly?cityId=1&year=2024&groupCode=residential
     */
    @GetMapping("/sale/m2/monthly")
    public Map<String, String> getMonthlyAveragePricePerSquareMeter(@RequestParam Long cityId,
                                                                    @RequestParam Integer year,
                                                                    @RequestParam(required = false) String groupCode,
                                                                    @RequestParam(required = false) String categoryCode) {
        return priceService.getMonthlyAveragePricePerSquareMeter(cityId, groupCode, categoryCode, year);
    }

}
