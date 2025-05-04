package com.realstate.divar.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class RealEstateAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AdGroup adGroup;

    @ManyToOne
    private AdCategory adCategory;

    @ManyToOne
    private City city;

    @ManyToOne
    private Neighborhood neighborhood;

    private LocalDateTime createdAtMonth;

    private String userType;
    @Column(length = 5000)
    private String description;
    private String title;

    private String rentMode;
    private Long rentValue;
    private Boolean rentToSingle;
    private String rentType;

    private String priceMode;
    private Long priceValue;

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

    private String buildingDirection;
    private Boolean hasPool;
    private Boolean hasJacuzzi;
    private Boolean hasSauna;

    private String floorMaterial;
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
