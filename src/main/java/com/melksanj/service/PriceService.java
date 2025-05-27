package com.melksanj.service;

import com.melksanj.common.DateUtils;
import com.melksanj.constants.AdCategoryEnum;
import com.melksanj.constants.AdGroupEnum;
import com.melksanj.repository.RealEstateAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final RealEstateAdRepository realEstateAdRepository;
    private final DecimalFormat decimalFormat = new DecimalFormat("#");

    public Map<String, String> getYearlyAverageSalePrices(Long cityId, String groupCode, String categoryCode, Integer regionId) {


        List<Object[]> rows = realEstateAdRepository.findYearlyAverageSalePrices(
                cityId,
                AdGroupEnum.fromCodeAndIsSell(groupCode, true),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, true),
                regionId
        );

        Map<String, String> result = new TreeMap<>();
        for (Object[] row : rows) {
            String year = DateUtils.toPersianYear((Integer) row[0]);
            String price = formatPrice((Double) row[1]);
            result.put(year, price);
        }
        return result;
    }

    public Map<String, String> getMonthlyAverageSalePrices(Long cityId, String groupCode, String categoryCode, Integer year) {
        List<Object[]> rows = realEstateAdRepository.findMonthlyAverageSalePrices(
                cityId,
                AdGroupEnum.fromCodeAndIsSell(groupCode, true),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, true),
                year
        );

        Map<String, String> result = new TreeMap<>();
        for (Object[] row : rows) {
            String yearMonth = DateUtils.toPersianYearMonth(year, (Integer) row[0]);
            String price = formatPrice((Double) row[1]);
            result.put(yearMonth, price);
        }
        return result;
    }

    public Map<String, Map<String, Long>> getYearlyRentStats(Long cityId, String groupCode, String categoryCode) {
        List<Object[]> rows = realEstateAdRepository.findYearlyAverageRentAndCredit(
                cityId,
                AdGroupEnum.fromCodeAndIsSell(groupCode, false),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, false)
        );

        Map<String, Map<String, Long>> result = new TreeMap<>();
        for (Object[] row : rows) {
            String year = DateUtils.toPersianYear((Integer) row[0]);
            result.put(year, buildRentMap((Double) row[1], (Double) row[2]));
        }
        return result;
    }

    public Map<String, Map<String, Long>> getMonthlyRentStats(Long cityId, Integer year, String groupCode, String categoryCode) {
        List<Object[]> rows = realEstateAdRepository.findMonthlyAverageRentAndCredit(
                cityId,
                year,
                AdGroupEnum.fromCodeAndIsSell(groupCode, false),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, false)
        );

        Map<String, Map<String, Long>> result = new TreeMap<>();
        for (Object[] row : rows) {
            String yearMonth = DateUtils.toPersianYearMonth(year, (Integer) row[0]);
            result.put(yearMonth, buildRentMap((Double) row[1], (Double) row[2]));
        }
        return result;
    }

    public Map<String, String> getYearlyAveragePricePerSquareMeter(Long cityId, String groupCode, String categoryCode) {
        List<Object[]> rows = realEstateAdRepository.findYearlyAveragePricePerSquareMeter(
                cityId,
                AdGroupEnum.fromCodeAndIsSell(groupCode, true),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, true)
        );

        Map<String, String> result = new TreeMap<>();
        for (Object[] row : rows) {
            String year = DateUtils.toPersianYear((Integer) row[0]);
            String pricePerM2 = formatPrice((Double) row[1]);
            result.put(year, pricePerM2);
        }
        return result;
    }

    public Map<String, String> getMonthlyAveragePricePerSquareMeter(Long cityId, String groupCode, String categoryCode, Integer year) {
        List<Object[]> rows = realEstateAdRepository.findMonthlyAveragePricePerSquareMeter(
                cityId,
                AdGroupEnum.fromCodeAndIsSell(groupCode, true),
                AdCategoryEnum.fromCodeAndIsSell(categoryCode, true),
                year
        );

        Map<String, String> result = new TreeMap<>();
        for (Object[] row : rows) {
            String yearMonth = DateUtils.toPersianYearMonth(year, (Integer) row[0]); // e.g. "1402/01"
            String price = formatPrice((Double) row[1]); // formatted billion tomans
            result.put(yearMonth, price);
        }

        return result;
    }

    private Map<String, Long> buildRentMap(Double credit, Double rent) {
        Map<String, Long> map = new HashMap<>();
        map.put("credit", toMillion(credit));
        map.put("rent", toMillion(rent));
        return map;
    }

    private Long toMillion(Double value) {
        return value != null ? Math.round(value / 1_000_000.0 * 10.0) / 10 : 0;
    }

    private String formatPrice(Double price) {
        if (price == null) return "-";

        if (price >= 1_000_000_000) {
            return decimalFormat.format(price / 1_000_000_000.0);
        } else if (price >= 1_000_000) {
            return decimalFormat.format(price / 1_000_000.0);
        } else {
            return decimalFormat.format(price) + " تومان";
        }
    }
}
