package com.melksanj.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
//cat2Slug
@Getter
@RequiredArgsConstructor
public enum AdGroupTypeValue {
    COMMERCIAL_RENT("commercial-rent", "تجاری برای اجاره"),
    COMMERCIAL_SELL("commercial-sell", "تجاری برای فروش"),
    REAL_ESTATE_SERVICES("real-estate-services", "خدمات ملکی"),
    RESIDENTIAL_RENT("residential-rent", "مسکونی برای اجاره"),
    RESIDENTIAL_SELL("residential-sell", "مسکونی برای فروش"),
    TEMPORARY_RENT("temporary-rent", "اجاره موقت");

    private final String code;
    private final String title;


    public static AdGroupTypeValue fromCode(String code) {
        for (AdGroupTypeValue category : values()) {
            if (category.code.equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid AdCategory code: " + code);
    }
}
