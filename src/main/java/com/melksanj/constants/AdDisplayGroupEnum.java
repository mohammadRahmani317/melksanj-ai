package com.melksanj.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdDisplayGroupEnum {
    RESIDENTIAL("residential", "مسکونی"),
    COMMERCIAL_RENT("commercial", "تجاری");
//    REAL_ESTATE_SERVICES("real-estate-services", "خدمات ملکی"),
//    TEMPORARY("temporary-rent", "اجاره موقت");

    private final String code;
    private final String title;
}
