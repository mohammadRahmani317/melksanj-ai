package com.melksanj.service;

import com.melksanj.common.DateUtils;
import com.melksanj.constants.AdCategoryEnum;
import com.melksanj.constants.AdGroupEnum;
import com.melksanj.repository.RealEstateAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Service
@RequiredArgsConstructor
public class PriceService {

    private final RealEstateAdRepository realEstateAdRepository;
    private final DecimalFormat decimalFormat = new DecimalFormat("#");


    public Map<String, String> getYearlyAveragePrices(Long cityId, String groupCode, String categoryCode) {
        List<String> groupCodes = AdGroupEnum.fromCodeAndIsSell(groupCode, true);
        List<String> categoryCodes = AdCategoryEnum.fromCodeAndIsSell(categoryCode, true);

        List<Object[]> rows = realEstateAdRepository.findAveragePricePerYear(cityId, groupCodes, categoryCodes);

        Map<String, String> result = new TreeMap<>();
        for (Object[] row : rows) {
            String key = DateUtils.toPersianYear((Integer) row[0]);
            String value = formatPrice((Double) row[1]);
            result.put(key, value);
        }

        return result;
    }

    public Map<String, String> getAveragePriceByMonthAndYear(Long cityId, String groupCode, String categoryCode, Integer year) {
        List<String> groupCodes = AdGroupEnum.fromCodeAndIsSell(groupCode, true);
        List<String> categoryCodes = AdCategoryEnum.fromCodeAndIsSell(categoryCode, true);

        List<Object[]> rows = realEstateAdRepository.findAveragePriceByMonthAndYear(cityId, groupCodes, categoryCodes, year);

        Map<String, String> result = new TreeMap<>();
        for (Object[] row : rows) {
            String key = DateUtils.toPersianYearMonth(year, (Integer) row[0]);
            String value = formatPrice((Double) row[1]);
            result.put(key, value);
        }

        return result;
    }


    private String formatPrice(Double price) {
        return decimalFormat.format(price / 1_000_000_000.0);
    }
}
