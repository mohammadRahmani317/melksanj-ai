package com.melksanj.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdDisplayCategoryEnum {
    APARTMENT("apartment", "آپارتمان"),
    HOUSE_VILLA("house-villa", "خانه/ویلا"),
    INDUSTRY_AGRICULTURE_BUSINESS("industry-agriculture-business", "صنعتی/کشاورزی/تجاری"),
    OFFICE("office", "دفتر کار"),
    PARTNERSHIP("partnership", "مشارکت در ساخت"),
    PLOT("plot", "زمین"),
    PRESELL("presell", "پروژه پیش‌فروش"),
    SHOP("shop", "مغازه"),
    SUITE("suite", "سوئیت"),
    VILLA("villa", "ویلا"),
    WORKSPACE("workspace", "فضای کاری");

    private final String code;
    private final String title;
}
