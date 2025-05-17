package com.melksanj.web;

import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.model.City;
import com.melksanj.service.MelksanjService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/melksanj")
@RequiredArgsConstructor
public class BaseController {

    private final MelksanjService melksanjService;

    /**
     * Get the list of all cities.
     *
     * @return List of City
     */
    @GetMapping("/meta/cities")
    public List<City> fetchAllCities() {
        return melksanjService.fetchAllCities();
    }

    /**
     * Get the list of all ad groups.
     *
     * @return List of AdGroupDTO
     */
    @GetMapping("/meta/groups")
    public List<AdGroupDTO> fetchAllAdGroups() {
        return melksanjService.fetchAllAdGroups();
    }

    /**
     * Get the list of all ad categories.
     *
     * @return List of AdCategoryDTO
     */
    @GetMapping("/meta/categories")
    public List<AdCategoryDTO> fetchAllAdCategories() {
        return melksanjService.fetchAllAdCategories();
    }


    @GetMapping("/years")
    public List<Integer> getAvailableYears() {
        return melksanjService.findDistinctYears();
    }
}
