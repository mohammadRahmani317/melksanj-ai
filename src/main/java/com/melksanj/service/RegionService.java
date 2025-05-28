package com.melksanj.service;

import com.melksanj.repository.RealEstateAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RealEstateAdRepository realEstateAdRepository;

    public Map<String, Map<String, Double>> getYearlySalePriceGrowthByRegion(Long cityId, String groupCode, String categoryCode) {
        List<Object[]> rawData = realEstateAdRepository.getYearlyAveragePricePerRegion(cityId, groupCode, categoryCode);

        // rawData => [regionName, year, avgPrice]
        Map<String, TreeMap<Integer, Double>> regionYearPriceMap = new HashMap<>();

        for (Object[] row : rawData) {
            String region = (String) row[0];
            Integer year = ((Number) row[1]).intValue();
            Double avgPrice = ((Number) row[2]).doubleValue();

            regionYearPriceMap
                    .computeIfAbsent(region, k -> new TreeMap<>())
                    .put(year, avgPrice);
        }

        // Calculate growth rate
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();
        for (Map.Entry<String, TreeMap<Integer, Double>> entry : regionYearPriceMap.entrySet()) {
            String region = entry.getKey();
            TreeMap<Integer, Double> yearPrice = entry.getValue();

            Map<String, Double> growthPerYear = new LinkedHashMap<>();
            Double lastPrice = null;
            for (Map.Entry<Integer, Double> yp : yearPrice.entrySet()) {
                Integer year = yp.getKey();
                Double price = yp.getValue();

                double growth = (lastPrice != null && lastPrice > 0) ? (price - lastPrice) / lastPrice : 0;
                growthPerYear.put(String.valueOf(year + 621), growth); // تبدیل سال شمسی به میلادی

                lastPrice = price;
            }

            result.put(region, growthPerYear);
        }
        return result;
    }
}
