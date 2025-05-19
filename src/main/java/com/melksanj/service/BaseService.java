package com.melksanj.service;

import com.melksanj.constants.AdDisplayCategoryEnum;
import com.melksanj.constants.AdDisplayGroupEnum;
import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.dto.YearDto;
import com.melksanj.model.City;
import com.melksanj.repository.CityRepository;
import com.melksanj.repository.RealEstateAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseService {

    private final RealEstateAdRepository realEstateAdRepository;
    private final CityRepository cityRepository;

    public List<City> fetchAllCities() {
        return cityRepository.findAll();
    }

    public List<AdGroupDTO> fetchAllAdGroups() {
        return Arrays.stream(AdDisplayGroupEnum.values())
                .map(adDisplayGroupEnum -> new AdGroupDTO(adDisplayGroupEnum.getCode(), adDisplayGroupEnum.getTitle()))
                .toList();
    }

    public List<AdCategoryDTO> fetchAllAdCategories() {
        return Arrays.stream(AdDisplayCategoryEnum.values())
                .map(adDisplayCategoryEnum -> new AdCategoryDTO(adDisplayCategoryEnum.getCode(), adDisplayCategoryEnum.getTitle()))
                .toList();

    }

    public List<YearDto> findDistinctYears() {

        List<Integer> years = realEstateAdRepository.findDistinctYears();
        return years.stream()
                .map(year -> {
                    // تبدیل سال میلادی به اولین روز از اون سال
                    LocalDate gregorianDate = LocalDate.of(year, 1, 1);
                    // تبدیل به شمسی
                    com.github.mfathi91.time.PersianDate persianDate = com.github.mfathi91.time.PersianDate.fromGregorian(gregorianDate);
                    return new YearDto(year, String.valueOf(persianDate.getYear()));
                })
                .collect(Collectors.toList());
    }


}
