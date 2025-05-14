package com.melksanj.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdDisplayGroupEnum {
    COMMERCIAL_RENT("commercial", "تجاری"),
    RESIDENTIAL("residential", "مسکونی"),
    REAL_ESTATE_SERVICES("real-estate-services", "خدمات ملکی"),
    TEMPORARY("temporary-rent", "اجاره موقت");

    private final String code;
    private final String title;
}
