package com.melksanj.web;

import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.dto.RegionDTO;
import com.melksanj.dto.YearDTO;
import com.melksanj.model.City;
import com.melksanj.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/melksanj/base")
@RequiredArgsConstructor
public class BaseController {

    private final BaseService melksanjService;

    /**
     * Retrieves a list of all available cities.
     *
     * @return a list of {@link City} entities.
     */
    @GetMapping("/cities")
    public List<City> fetchAllCities() {
        return melksanjService.fetchAllCities();
    }

    /**
     * Retrieves a list of all available advertisement groups.
     *
     * @return a list of {@link AdGroupDTO}.
     */
    @GetMapping("/groups")
    public List<AdGroupDTO> fetchAllAdGroups() {
        return melksanjService.fetchAllAdGroups();
    }

    /**
     * Retrieves a list of all available advertisement categories.
     *
     * @return a list of {@link AdCategoryDTO}.
     */
    @GetMapping("/categories")
    public List<AdCategoryDTO> fetchAllAdCategories() {
        return melksanjService.fetchAllAdCategories();
    }

    /**
     * Retrieves a list of distinct years extracted from the real estate ads.
     *
     * @return a list of {@link YearDTO} representing available years.
     */
    @GetMapping("/years")
    public List<YearDTO> getAvailableYears() {
        return melksanjService.findDistinctYears();
    }

    /**
     * Retrieves a list of regions (districts) for a given city ID.
     * This endpoint is typically used to populate region dropdowns
     * or filters in analytics dashboards or UI forms.
     *
     * @param cityId The ID of the city for which to fetch regions.
     * @return A list of RegionDTOs representing regions in the specified city.
     */
    @GetMapping("/regions")
    public List<RegionDTO> getRegionsByCity(@RequestParam Long cityId) {
        return melksanjService.getRegionsByCity(cityId);
    }
}
