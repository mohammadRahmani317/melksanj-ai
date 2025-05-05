package com.realstate.divar.service;

import com.github.mfathi91.time.PersianDate;
import com.realstate.divar.common.AdCategoryTypeValue;
import com.realstate.divar.common.AdGroupTypeValue;
import com.realstate.divar.dto.AdCategoryDTO;
import com.realstate.divar.dto.AdGroupDTO;
import com.realstate.divar.model.*;
import com.realstate.divar.repository.*;
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
public class RealEstateAdService {


    private final RealEstateAdRepository realEstateAdRepository;
    private final NeighborhoodRepository neighborhoodRepository;
    private final CityRepository cityRepository;
    private final AdCategoryRepository adCategoryRepository;
    private final AdGroupRepository adGroupRepository;


    private final Map<String, AdGroup> groupCache = new HashMap<>();
    private final Map<String, AdCategory> categoryCache = new HashMap<>();


    @Transactional
    public void loadFile() {
        String fileName = "real_estate_ads.csv";
        try (
                InputStreamReader isr = new InputStreamReader(
                        getClass().getClassLoader().getResourceAsStream(fileName),
                        StandardCharsets.UTF_8
                );
                CSVParser parser = new CSVParser(isr, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<RealEstateAd> batch = new ArrayList<>();
            int batchSize = 500;
            int recordIndex = 0;
            long count = 0L;

            for (CSVRecord record : parser) {
                try {
                    RealEstateAd ad = mapRecordToEntity(record);
                    batch.add(ad);

                    if (batch.size() >= batchSize) {
                        realEstateAdRepository.saveAll(batch);
                        batch.clear();
                        count += batchSize;
                        log.info("Count of record inserted {}", count);
                    }
                } catch (Exception e) {
                    log.warn("⛔️ خطا در پارس رکورد شماره {} - پیام: {}", recordIndex, e.getMessage());
                }
                recordIndex++;
                if (count == 500L)
                    break;
            }

            if (!batch.isEmpty()) {
                realEstateAdRepository.saveAll(batch);
            }
            log.info("✅ بارگذاری فایل با موفقیت انجام شد: {}", fileName);
        } catch (Exception e) {
            log.error("❌ خطا در بارگذاری فایل {}: {}", fileName, e.getMessage(), e);
        }
    }

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    private RealEstateAd mapRecordToEntity(CSVRecord r) {
        RealEstateAd realEstateAd = new RealEstateAd();
        AdGroup adGroup = getOrCreateGroup(getString(r, "cat2_slug"));
        setField(() -> realEstateAd.setAdGroup(adGroup));
        setField(() -> realEstateAd.setAdCategory(getOrCreateCategory(getString(r, "cat3_slug"), adGroup)));
        setField(() -> realEstateAd.setCity(getCity(r.get("city_slug"))));
        setField(() -> realEstateAd.setNeighborhood(getNeighborhood(r.get("neighborhood_slug"), realEstateAd.getCity())));
        setField(() -> realEstateAd.setCreatedAtMonth(convertToLocalDateTime(getString(r, "created_at_month"))));
        setField(() -> realEstateAd.setUserType(getString(r, "user_type")));
        setField(() -> realEstateAd.setDescription(getString(r, "description")));
        setField(() -> realEstateAd.setTitle(getString(r, "title")));
        setField(() -> realEstateAd.setRentMode(getString(r, "rent_mode")));
        setField(() -> realEstateAd.setRentValue(getLong(r, "rent_value")));
        setField(() -> realEstateAd.setRentToSingle(getBoolean(r, "rent_to_single")));
        setField(() -> realEstateAd.setRentType(getString(r, "rent_type")));
        setField(() -> realEstateAd.setPriceMode(getString(r, "price_mode")));
        setField(() -> realEstateAd.setPriceValue(getLong(r, "price_value")));
        setField(() -> realEstateAd.setCreditMode(getString(r, "credit_mode")));
        setField(() -> realEstateAd.setCreditValue(getLong(r, "credit_value")));
        setField(() -> realEstateAd.setRentCreditTransform(getBoolean(r, "rent_credit_transform")));
        setField(() -> realEstateAd.setBuildingSize(getDouble(r, "building_size")));
        setField(() -> realEstateAd.setLandSize(getDouble(r, "building_size")));
        setField(() -> realEstateAd.setTransformablePrice(getLong(r, "transformable_price")));
        setField(() -> realEstateAd.setTransformableCredit(getLong(r, "transformable_credit")));
        setField(() -> realEstateAd.setTransformedCredit(getLong(r, "transformed_credit")));
        setField(() -> realEstateAd.setTransformableRent(getLong(r, "transformable_rent")));
        setField(() -> realEstateAd.setTransformedRent(getLong(r, "transformed_rent")));
        setField(() -> realEstateAd.setLocationLatitude(getDouble(r, "location_latitude")));
        setField(() -> realEstateAd.setLocationLongitude(getDouble(r, "location_longitude")));
        setField(() -> realEstateAd.setLocationRadius(getDouble(r, "location_radius")));

        return realEstateAd;
    }

    private AdGroup getOrCreateGroup(String cat2Slug) {
        return groupCache.computeIfAbsent(cat2Slug, code ->
                adGroupRepository.findByCode(code)
                        .orElseGet(() -> adGroupRepository.save(new AdGroup(null, code, toTitleAdGroup(code))))
        );
    }

    private String toTitleAdGroup(String code) {
        return AdGroupTypeValue.fromCode(code).getTitle();
    }

    private AdCategory getOrCreateCategory(String cat3Slug, AdGroup adGroup) {
        return categoryCache.computeIfAbsent(cat3Slug, code ->
                adCategoryRepository.findByCode(code)
                        .orElseGet(() -> adCategoryRepository.save(new AdCategory(null, code, toTitleAdCategory(code), adGroup)))
        );
    }

    private String toTitleAdCategory(String code) {
        return AdCategoryTypeValue.fromCode(code).getTitle();
    }

    private City getCity(String cityName) {
        if (cityName != null && !cityName.isEmpty()) {
            Optional<City> existing = cityRepository.findByName(cityName);
            if (existing.isPresent()) {
                return existing.get();
            }
            City city = new City();
            city.setName(cityName);
            return cityRepository.save(city);
        }
        return null;
    }

    private Neighborhood getNeighborhood(String neighborhoodName, City city) {
        if (neighborhoodName != null && !neighborhoodName.isEmpty() && city != null) {
            Optional<Neighborhood> existing = neighborhoodRepository.findByNameAndCity(neighborhoodName, city);
            if (existing.isPresent()) {
                return existing.get();
            }
            Neighborhood neighborhood = new Neighborhood();
            neighborhood.setName(neighborhoodName);
            neighborhood.setCity(city);
            return neighborhoodRepository.save(neighborhood);
        }
        return null;
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

    private Integer getInteger(CSVRecord r, String name) {
        return r.isMapped(name) && !r.get(name).isEmpty() ? Integer.parseInt(r.get(name)) : null;
    }

    private Double getDouble(CSVRecord r, String name) {
        return r.isMapped(name) && !r.get(name).isEmpty() ? Double.parseDouble(r.get(name)) : null;
    }

    private Boolean getBoolean(CSVRecord r, String name) {
        return r.isMapped(name) && !r.get(name).isEmpty() ? Boolean.parseBoolean(r.get(name)) : null;
    }

    public static LocalDateTime convertToLocalDateTime(String value) {
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("M/d/yyyy H:mm"),
                DateTimeFormatter.ofPattern("M/d/yyyy HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"), // fallback for date-only
                DateTimeFormatter.ofPattern("yyyy/MM/dd")  // fallback for date-only
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException e1) {
                try {
                    // Try parse as LocalDate if LocalDateTime fails
                    return LocalDate.parse(value, formatter).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                }
            }
        }
        return null; // or throw a custom exception if needed
    }


    public Map<String, String> getCharPerYear(Long cityId, String groupCode, String categoryCode) {

        List<RealEstateAd> ads = realEstateAdRepository.findByCityAndAdGroupAndAdCategoryAndPriceValueIsNotNull(cityId, groupCode, categoryCode);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return ads.stream()
                .collect(Collectors.groupingBy(
                        ad -> {
                            LocalDate createdAt = ad.getCreatedAtMonth().toLocalDate();
                            PersianDate persianDate = PersianDate.fromGregorian(createdAt);
                            return String.valueOf(persianDate.getYear()); // فقط سال شمسی
                        },
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.averagingLong(RealEstateAd::getPriceValue),
                                avg -> decimalFormat.format(avg / 1_000_000_000.0)
                        )
                ));
    }

    public List<AdGroupDTO> getGroups() {
        return adGroupRepository.findAll()
                .stream()
                .map(adGroup -> {
                    AdGroupDTO adGroupDTO = new AdGroupDTO();
                    adGroupDTO.setCode(adGroup.getCode());
                    adGroupDTO.setTitle(adGroup.getTitle());
                    return adGroupDTO;
                }).toList();
    }

    public List<AdCategoryDTO> getCategory() {
        return adCategoryRepository.findAll()
                .stream()
                .map(adCategory -> {
                    AdCategoryDTO adCategoryDTO = new AdCategoryDTO();
                    adCategoryDTO.setCode(adCategory.getCode());
                    adCategoryDTO.setTitle(adCategory.getTitle());
                    return adCategoryDTO;
                }).toList();
    }
}
