package com.realstate.divar.web;

import com.realstate.divar.dto.AdCategoryDTO;
import com.realstate.divar.dto.AdGroupDTO;
import com.realstate.divar.model.City;
import com.realstate.divar.service.RealEstateAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/reale/state")
@RequiredArgsConstructor
public class RealEstateAdController {
    private final RealEstateAdService realEstateAdService;

    @PostMapping("/fill/data")
    public void fillAllDataFromCsvFile() {
        realEstateAdService.loadFile();
    }

    @GetMapping("/chart")
    public Map<String, String> getRealEstateAdControllerChartImageByCityPerYear(@RequestParam Long cityId,
                                                                                @RequestParam(required = false) String groupCode,
                                                                                @RequestParam(required = false) String categoryCode) {
        return realEstateAdService.getCharPerYear(cityId, groupCode, categoryCode);
    }

    @GetMapping("/cities")
    public List<City> getCities() throws IOException {
        return realEstateAdService.getCities();
    }

    @GetMapping("/groups")
    public List<AdGroupDTO> getGroups() {
        return realEstateAdService.getGroups();
    }

    @GetMapping("/categories")
    public List<AdCategoryDTO> getCategory() {
        return realEstateAdService.getCategory();
    }


}
