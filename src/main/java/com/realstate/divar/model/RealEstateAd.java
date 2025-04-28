package com.realstate.divar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
public class RealEstateAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 5000)
    private String cat2Slug;
    @Column(length = 5000)
    private String cat3Slug;
    @Column(length = 5000)
    private String citySlug;
    @Column(length = 5000)
    private String neighborhoodSlug;
    @Column(length = 5000)
    private LocalDateTime createdAtMonth;
    @Column(length = 5000)
    private String userType;
    @Column(length = 5000)
    private String description;
    @Column(length = 5000)
    private String title;
    @Column(length = 5000)
    private String rentMode;
    private Long rentValue;
    private Boolean rentToSingle;
    @Column(length = 5000)
    private String rentType;
    @Column(length = 5000)
    private String priceMode;
    private Long priceValue;
    @Column(length = 5000)
    private String creditMode;
    private Long creditValue;
    private Boolean rentCreditTransform;
    private Long transformablePrice;
    private Long transformableCredit;
    private Long transformedCredit;
    private Long transformableRent;
    private Long transformedRent;
    private Double landSize;
    private Double buildingSize;
    @Column(length = 5000)
    private String deedType;
    private Boolean hasBusinessDeed;
    private Integer floor;
    private Integer roomsCount;
    private Integer totalFloorsCount;
    private Integer unitPerFloor;
    private Boolean hasBalcony;
    private Boolean hasElevator;
    private Boolean hasWarehouse;
    private Boolean hasParking;
    private Integer constructionYear;
    private Boolean isRebuilt;
    private Boolean hasWater;
    private Boolean hasWarmWaterProvider;
    private Boolean hasElectricity;
    private Boolean hasGas;
    private Boolean hasHeatingSystem;
    private Boolean hasCoolingSystem;
    private Boolean hasRestroom;
    private Boolean hasSecurityGuard;
    private Boolean hasBarbecue;
    @Column(length = 5000)
    private String buildingDirection;
    private Boolean hasPool;
    private Boolean hasJacuzzi;
    private Boolean hasSauna;
    @Column(length = 5000)
    private String floorMaterial;
    @Column(length = 5000)
    private String propertyType;
    private Integer regularPersonCapacity;
    private Integer extraPersonCapacity;
    private Long costPerExtraPerson;
    private Long rentPriceOnRegularDays;
    private Long rentPriceOnSpecialDays;
    private Long rentPriceAtWeekends;
    private Double locationLatitude;
    private Double locationLongitude;
    private Double locationRadius;
}
