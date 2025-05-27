package com.melksanj.service;

import com.melksanj.constants.AdDisplayCategoryEnum;
import com.melksanj.constants.AdDisplayGroupEnum;
import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.dto.RegionDTO;
import com.melksanj.dto.YearDTO;
import com.melksanj.model.City;
import com.melksanj.model.Neighborhood;
import com.melksanj.repository.CityRepository;
import com.melksanj.repository.NeighborhoodRepository;
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
    private final NeighborhoodRepository neighborhoodRepository;

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

    public List<YearDTO> findDistinctYears() {

        List<Integer> years = realEstateAdRepository.findDistinctYears();
        return years.stream()
                .map(year -> {
                    // تبدیل سال میلادی به اولین روز از اون سال
                    LocalDate gregorianDate = LocalDate.of(year, 1, 1);
                    // تبدیل به شمسی
                    com.github.mfathi91.time.PersianDate persianDate = com.github.mfathi91.time.PersianDate.fromGregorian(gregorianDate);
                    return new YearDTO(year, String.valueOf(persianDate.getYear()));
                })
                .collect(Collectors.toList());
    }


    public List<RegionDTO> getRegionsByCity(Long cityId) {
        List<Integer> regions = neighborhoodRepository.findDistinctRegionsByCityId(cityId);
        return regions.stream()
                .filter(f->!f.equals(0))
                .map(r -> new RegionDTO(r, "منطقه " + r))
                .collect(Collectors.toList());
    }
}
