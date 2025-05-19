package com.melksanj.web;

import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.dto.YearDto;
import com.melksanj.model.City;
import com.melksanj.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @return a list of {@link YearDto} representing available years.
     */
    @GetMapping("/years")
    public List<YearDto> getAvailableYears() {
        return melksanjService.findDistinctYears();
    }
}
