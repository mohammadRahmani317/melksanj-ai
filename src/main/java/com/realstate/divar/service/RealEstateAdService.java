package com.realstate.divar.service;

import com.realstate.divar.model.RealEstateAd;
import com.realstate.divar.repository.RealEstateAdRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealEstateAdService {


    private final RealEstateAdRepository realEstateAdRepository;


    public void loadCsvFiles() {
        String[] files = {"real_estate_ads.csv"};
        for (String fileName : files) {
            loadFile(fileName);
        }
    }

    private void loadFile(String fileName) {
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

            Long count = 0L;

            for (CSVRecord record : parser) {
                try {
                    RealEstateAd ad = mapRecordToEntity(record);
                    batch.add(ad);

                    if (batch.size() >= batchSize) {
                        realEstateAdRepository.saveAll(batch);
                        realEstateAdRepository.flush();
                        batch.clear();
                        count = count + 500;
                    }

                } catch (Exception e) {
                    log.warn("⛔️ خطا در پارس رکورد شماره {} - پیام: {}", recordIndex, e.getMessage());
                }
                recordIndex++;
            }

            if (!batch.isEmpty()) {
                realEstateAdRepository.saveAll(batch);
                realEstateAdRepository.flush();
            }

            log.info("✅ بارگذاری فایل با موفقیت انجام شد: {}", fileName);
        } catch (Exception e) {
            log.error("❌ خطا در بارگذاری فایل {}: {}", fileName, e.getMessage(), e);
        }
    }

    private RealEstateAd mapRecordToEntity(CSVRecord r) {
        RealEstateAd ad = new RealEstateAd();

        setField(() -> ad.setCat2Slug(getString(r, "cat2_slug")));
        setField(() -> ad.setCat3Slug(getString(r, "cat3_slug")));
        setField(() -> ad.setCitySlug(getString(r, "city_slug")));
        setField(() -> ad.setNeighborhoodSlug(getString(r, "neighborhood_slug")));
        setField(() -> ad.setCreatedAtMonth(convertToLocalDateTime(getString(r, "created_at_month"))));
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
        setField(() -> ad.setTransformablePrice(getLong(r, "transformable_price")));
        setField(() -> ad.setTransformableCredit(getLong(r, "transformable_credit")));
        setField(() -> ad.setTransformedCredit(getLong(r, "transformed_credit")));
        setField(() -> ad.setTransformableRent(getLong(r, "transformable_rent")));
        setField(() -> ad.setTransformedRent(getLong(r, "transformed_rent")));
        setField(() -> ad.setLandSize(getDouble(r, "land_size")));
        setField(() -> ad.setBuildingSize(getDouble(r, "building_size")));
        setField(() -> ad.setDeedType(getString(r, "deed_type")));
        setField(() -> ad.setHasBusinessDeed(getBoolean(r, "has_business_deed")));
        setField(() -> ad.setFloor(getInteger(r, "floor")));
        setField(() -> ad.setRoomsCount(getInteger(r, "rooms_count")));
        setField(() -> ad.setTotalFloorsCount(getInteger(r, "total_floors_count")));
        setField(() -> ad.setUnitPerFloor(getInteger(r, "unit_per_floor")));
        setField(() -> ad.setHasBalcony(getBoolean(r, "has_balcony")));
        setField(() -> ad.setHasElevator(getBoolean(r, "has_elevator")));
        setField(() -> ad.setHasWarehouse(getBoolean(r, "has_warehouse")));
        setField(() -> ad.setHasParking(getBoolean(r, "has_parking")));
        setField(() -> ad.setConstructionYear(getInteger(r, "construction_year")));
        setField(() -> ad.setIsRebuilt(getBoolean(r, "is_rebuilt")));
        setField(() -> ad.setHasWater(getBoolean(r, "has_water")));
        setField(() -> ad.setHasWarmWaterProvider(getBoolean(r, "has_warm_water_provider")));
        setField(() -> ad.setHasElectricity(getBoolean(r, "has_electricity")));
        setField(() -> ad.setHasGas(getBoolean(r, "has_gas")));
        setField(() -> ad.setHasHeatingSystem(getBoolean(r, "has_heating_system")));
        setField(() -> ad.setHasCoolingSystem(getBoolean(r, "has_cooling_system")));
        setField(() -> ad.setHasRestroom(getBoolean(r, "has_restroom")));
        setField(() -> ad.setHasSecurityGuard(getBoolean(r, "has_security_guard")));
        setField(() -> ad.setHasBarbecue(getBoolean(r, "has_barbecue")));
        setField(() -> ad.setBuildingDirection(getString(r, "building_direction")));
        setField(() -> ad.setHasPool(getBoolean(r, "has_pool")));
        setField(() -> ad.setHasJacuzzi(getBoolean(r, "has_jacuzzi")));
        setField(() -> ad.setHasSauna(getBoolean(r, "has_sauna")));
        setField(() -> ad.setFloorMaterial(getString(r, "floor_material")));
        setField(() -> ad.setPropertyType(getString(r, "property_type")));
        setField(() -> ad.setRegularPersonCapacity(getInteger(r, "regular_person_capacity")));
        setField(() -> ad.setExtraPersonCapacity(getInteger(r, "extra_person_capacity")));
        setField(() -> ad.setCostPerExtraPerson(getLong(r, "cost_per_extra_person")));
        setField(() -> ad.setRentPriceOnRegularDays(getLong(r, "rent_price_on_regular_days")));
        setField(() -> ad.setRentPriceOnSpecialDays(getLong(r, "rent_price_on_special_days")));
        setField(() -> ad.setRentPriceAtWeekends(getLong(r, "rent_price_at_weekends")));
        setField(() -> ad.setLocationLatitude(getDouble(r, "location_latitude")));
        setField(() -> ad.setLocationLongitude(getDouble(r, "location_longitude")));
        setField(() -> ad.setLocationRadius(getDouble(r, "location_radius")));

        return ad;
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

    private LocalDateTime convertToLocalDateTime(String value) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDate.parse(value, formatter).atStartOfDay();
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Double> generatePriceTrendChart(String citySlug) {
        //todo uncomment this after corrected database and all thing in your server
//        List<RealEstateAd> ads = realEstateAdRepository.findByCitySlugAndPriceValueIsNotNull(citySlug);
        List<RealEstateAd> ads = generateMockAds();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");

        return ads.stream()
                .filter(ad -> ad.getCreatedAtMonth() != null && ad.getPriceValue() != null)
                .collect(Collectors.groupingBy(
                        ad -> ad.getCreatedAtMonth().format(formatter),
                        TreeMap::new,
                        Collectors.averagingLong(RealEstateAd::getPriceValue)
                ));
    }

    public static List<RealEstateAd> generateMockAds() {
        List<RealEstateAd> ads = new ArrayList<>();

        // مثلا برای ۵ سال گذشته، هر سال ۵ تا آگهی درست کنیم
        for (int year = 2020; year <= 2024; year++) {
            for (int i = 0; i < 5; i++) {
                RealEstateAd ad = new RealEstateAd();
                ad.setPriceValue(50000000L + (long) (Math.random() * 100000000)); // قیمت رندوم
                ad.setCreatedAtMonth(LocalDate.of(year, (int)(Math.random() * 12) + 1, 1).atStartOfDay()); // یک ماه تصادفی

                ads.add(ad);
            }
        }

        return ads;
    }
}
