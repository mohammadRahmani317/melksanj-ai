package com.melksanj.service;

import com.github.mfathi91.time.PersianDate;
import com.melksanj.constants.*;
import com.melksanj.dto.AdCategoryDTO;
import com.melksanj.dto.AdGroupDTO;
import com.melksanj.model.*;
import com.melksanj.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MelksanjService {

    private final RealEstateAdRepository realEstateAdRepository;
    private final NeighborhoodRepository neighborhoodRepository;
    private final CityRepository cityRepository;
    private final AdCategoryRepository adCategoryRepository;
    private final AdGroupRepository adGroupRepository;

    private final Map<String, AdGroup> groupCache = new HashMap<>();
    private final Map<String, AdCategory> categoryCache = new HashMap<>();

    @Transactional
    public void importCsvData() {
        String fileName = "real_estate_ads.csv";
        try (
                InputStreamReader isr = new InputStreamReader(
                        getClass().getClassLoader().getResourceAsStream(fileName),
                        StandardCharsets.UTF_8
                );
                CSVParser parser = new CSVParser(isr, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<RealEstateAd> adsBatch = new ArrayList<>();
            int batchSize = 500;
            int recordIndex = 0;
            long insertedCount = 0L;

            for (CSVRecord record : parser) {
                try {
                    RealEstateAd ad = mapRecordToEntity(record);
                    adsBatch.add(ad);

                    if (adsBatch.size() >= batchSize) {
                        realEstateAdRepository.saveAll(adsBatch);
                        adsBatch.clear();
                        insertedCount += batchSize;
                        log.info("✅ {} records inserted", insertedCount);
                    }
                } catch (Exception e) {
                    log.warn("⛔️ Error parsing record {} - {}", recordIndex, e.getMessage());
                }
                recordIndex++;
                if (insertedCount == 500L) break;
            }

            if (!adsBatch.isEmpty()) {
                realEstateAdRepository.saveAll(adsBatch);
            }
            log.info("✅ CSV import completed: {}", fileName);
        } catch (Exception e) {
            log.error("❌ Error importing file {}: {}", fileName, e.getMessage(), e);
        }
    }

    public List<City> fetchAllCities() {
        return cityRepository.findAll();
    }

    public List<AdGroupDTO> fetchAllAdGroups() {
        return adGroupRepository.findAll()
                .stream()
                .map(adGroup -> new AdGroupDTO(adGroup.getCode(), adGroup.getTitle()))
                .toList();
    }

    public List<AdCategoryDTO> fetchAllAdCategories() {
        return adCategoryRepository.findAll()
                .stream()
                .map(adCategory -> new AdCategoryDTO(adCategory.getCode(), adCategory.getTitle()))
                .toList();
    }

    public Map<String, String> getYearlyAveragePrices(Long cityId, String groupCode, String categoryCode) {
        List<RealEstateAd> ads = realEstateAdRepository.findByCityAndAdGroupAndAdCategoryAndPriceValueIsNotNull(cityId, groupCode, categoryCode);
        DecimalFormat df = new DecimalFormat("#");

        return ads.stream()
                .collect(Collectors.groupingBy(
                        ad -> String.valueOf(PersianDate.fromGregorian(ad.getCreatedAtMonth().toLocalDate()).getYear()),
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.averagingLong(RealEstateAd::getPriceValue),
                                avg -> df.format(avg / 1_000_000_000.0)
                        )
                ));
    }

    private RealEstateAd mapRecordToEntity(CSVRecord r) {
        RealEstateAd ad = new RealEstateAd();
        AdGroup group = getOrCreateGroup(getString(r, "cat2_slug"));

        setField(() -> ad.setAdGroup(group));
        setField(() -> ad.setAdCategory(getOrCreateCategory(getString(r, "cat3_slug"), group)));
        setField(() -> ad.setCity(fetchOrCreateCity(r.get("city_slug"))));
        setField(() -> ad.setNeighborhood(fetchOrCreateNeighborhood(r.get("neighborhood_slug"), ad.getCity())));
        setField(() -> ad.setCreatedAtMonth(parseDateTime(getString(r, "created_at_month"))));

        setField(() -> ad.setUserType(getString(r, "user_type")));
        setField(() -> ad.setDescription(getString(r, "description")));
        setField(() -> ad.setTitle(getString(r, "title")));
        setField(() -> ad.setRentMode(getString(r, "rent_mode")));
        setField(() -> ad.setRentValue(getLong(r, "rent_value")));
        setField(() -> ad.setRentToSingle(getBoolean(r, "rent_to_single")));
        setField(() -> ad.setRentType(getString(r, "rent_type")));
        setField(() -> ad.setPriceMode(getString(r, "price_mode")));
        setField(() -> ad.setPriceValue(getLong(r, "price_value")));
        setField(() -> ad.setCreditMode(getString(r, "credit_mode")));
        setField(() -> ad.setCreditValue(getLong(r, "credit_value")));
        setField(() -> ad.setRentCreditTransform(getBoolean(r, "rent_credit_transform")));
        setField(() -> ad.setBuildingSize(getDouble(r, "building_size")));
        setField(() -> ad.setLandSize(getDouble(r, "building_size")));
        setField(() -> ad.setTransformablePrice(getLong(r, "transformable_price")));
        setField(() -> ad.setTransformableCredit(getLong(r, "transformable_credit")));
        setField(() -> ad.setTransformedCredit(getLong(r, "transformed_credit")));
        setField(() -> ad.setTransformableRent(getLong(r, "transformable_rent")));
        setField(() -> ad.setTransformedRent(getLong(r, "transformed_rent")));
        setField(() -> ad.setLocationLatitude(getDouble(r, "location_latitude")));
        setField(() -> ad.setLocationLongitude(getDouble(r, "location_longitude")));
        setField(() -> ad.setLocationRadius(getDouble(r, "location_radius")));

        return ad;
    }

    private AdGroup getOrCreateGroup(String slug) {
        return groupCache.computeIfAbsent(slug, code ->
                adGroupRepository.findByCode(code)
                        .orElseGet(() -> adGroupRepository.save(new AdGroup(null, code, AdGroupEnum.fromCode(code).getTitle())))
        );
    }

    private AdCategory getOrCreateCategory(String slug, AdGroup group) {
        return categoryCache.computeIfAbsent(slug, code ->
                adCategoryRepository.findByCode(code)
                        .orElseGet(() -> adCategoryRepository.save(new AdCategory(null, code, AdCategoryEnum.fromCode(code).getTitle(), group)))
        );
    }

    private City fetchOrCreateCity(String name) {
        return cityRepository.findByName(name)
                .orElseGet(() -> {
                    String nameFa = findCityFaBySlug(name);
                    return cityRepository.save(new City(null, name, nameFa));
                });
    }

    private String findCityFaBySlug(String slug) {
        try {
            return CityEnum.valueOf(slug.toUpperCase().replace("-", "_")).getNameFa();
        } catch (IllegalArgumentException e) {
            return "نامشخص";
        }
    }

    private Neighborhood fetchOrCreateNeighborhood(String slug, City city) {
        return neighborhoodRepository.findByNameAndCity(slug, city)
                .orElseGet(() -> {
                    Neighborhood n = new Neighborhood();
                    n.setName(slug);

                    Optional<NeighborhoodInfo> infoOpt = NeighborhoodEnumHandler.findNeighborhood(slug, city.getNameFa());

                    n.setNameFa(infoOpt.map(NeighborhoodInfo::getNameFa).orElse("نامشخص"));
                    n.setRegion(infoOpt.map(NeighborhoodInfo::getRegion).orElse(0));
                    n.setCity(city);

                    return neighborhoodRepository.save(n);
                });
    }

    private void setField(Runnable setter) {
        try {
            setter.run();
        } catch (Exception ignored) {
        }
    }

    private String getString(CSVRecord r, String name) {
        return r.isMapped(name) && !r.get(name).isEmpty() ? r.get(name) : null;
    }

    private Long getLong(CSVRecord r, String name) {
        return r.isMapped(name) && !r.get(name).isEmpty() ? Long.parseLong(r.get(name)) : null;
    }

    private Double getDouble(CSVRecord r, String name) {
        return r.isMapped(name) && !r.get(name).isEmpty() ? Double.parseDouble(r.get(name)) : null;
    }

    private Boolean getBoolean(CSVRecord r, String name) {
        return r.isMapped(name) && !r.get(name).isEmpty() ? Boolean.parseBoolean(r.get(name)) : null;
    }

    private LocalDateTime parseDateTime(String value) {
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("M/d/yyyy H:mm"),
                DateTimeFormatter.ofPattern("M/d/yyyy HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException e1) {
                try {
                    return LocalDate.parse(value, formatter).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                }
            }
        }
        return null;
    }
}
