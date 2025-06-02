package com.melksanj.service;

import com.melksanj.common.DateUtils;
import com.melksanj.constants.AdCategoryEnum;
import com.melksanj.constants.AdGroupEnum;
import com.melksanj.repository.RealEstateAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RealEstateAdRepository realEstateAdRepository;

    public Map<String, Map<String, String>> getYearlyGrowthRateByRegion(Long cityId,
                                                                        String groupCode,
                                                                        String categoryCode,
                                                                        Integer year) {

        List<Object[]> rows = realEstateAdRepository.findYearlyAveragePricePerSquareMeterGroupedByRegionBetweenYears(
                cityId,
                AdGroupEnum.fromCodeAndIsSell(groupCode, true),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, true),
                year - 1,
                year
        );

   /*     List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{1, year - 1, 100.0});
        rows.add(new Object[]{1, year, 120.0});
        rows.add(new Object[]{2, year - 1, 200.0});
        rows.add(new Object[]{2, year, 210.0});
        rows.add(new Object[]{3, year - 1, 150.0});
        rows.add(new Object[]{3, year, 150.0});*/

        Map<Integer, Map<Integer, Double>> pricesByYearAndRegion = new HashMap<>();

        for (Object[] row : rows) {
            Integer regionId = (Integer) row[0];
            Integer rowYear = (Integer) row[1];
            Double avgPrice = (Double) row[2];

            pricesByYearAndRegion
                    .computeIfAbsent(rowYear, y -> new HashMap<>())
                    .put(regionId, avgPrice);
        }

        Map<String, Map<String, String>> growthResult = new TreeMap<>();
        String yearShamsi = DateUtils.toPersianYear(year);
        Map<String, String> regionGrowthMap = new TreeMap<>();

        Map<Integer, Double> currentYearPrices = pricesByYearAndRegion.getOrDefault(year, Collections.emptyMap());
        Map<Integer, Double> prevYearPrices = pricesByYearAndRegion.getOrDefault(year - 1, Collections.emptyMap());

        for (Map.Entry<Integer, Double> entry : currentYearPrices.entrySet()) {
            Integer regionId = entry.getKey();
            Double currentPrice = entry.getValue();
            Double prevPrice = prevYearPrices.get(regionId);

            if (prevPrice == null || prevPrice == 0) {
                continue;
            }

            double growthRate = (currentPrice - prevPrice) / prevPrice * 100;
            String regionName = (regionId == null || regionId == 0) ? "نامشخص" : "منطقه " + regionId;
            String growthStr = String.format("%.2f%%", growthRate);

            regionGrowthMap.put(regionName, growthStr);
        }

        growthResult.put(yearShamsi, regionGrowthMap);

        return growthResult;
    }

    public Map<Integer, Map<String, Object>> getRegionDistribution(Long cityId, String groupCode, String categoryCode) {

        List<Object[]> rows = realEstateAdRepository.findRegionDistributionGroupedByRegionAndYear(
                cityId,
                AdGroupEnum.fromCodeAndIsSell(groupCode, true),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, true)
        );

        Map<Integer, Map<String, Object>> result = new LinkedHashMap<>();

        for (Object[] row : rows) {
            Integer regionId = (Integer) row[0];
            Long count = (Long) row[1];

            Map<String, Object> inner = new HashMap<>();
            inner.put("name", "منطقه" + regionId);
            inner.put("count", count);
            result.put(regionId, inner);
        }

        return result;
    }


}
