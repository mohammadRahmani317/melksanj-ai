package com.melksanj.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.from.StandardTableGroup;

import java.util.Arrays;
import java.util.Optional;

//cat2Slug
@Getter
@RequiredArgsConstructor
public enum AdGroupEnum {
    COMMERCIAL_RENT("commercial-rent", "تجاری برای اجاره"),
    COMMERCIAL_SELL("commercial-sell", "تجاری برای فروش"),
    REAL_ESTATE_SERVICES("real-estate-services", "خدمات ملکی"),
    RESIDENTIAL_RENT("residential-rent", "مسکونی برای اجاره"),
    RESIDENTIAL_SELL("residential-sell", "مسکونی برای فروش"),
    TEMPORARY_RENT("temporary-rent", "اجاره موقت");

    private final String code;
    private final String title;


    public static AdGroupEnum fromCode(String code) {
        for (AdGroupEnum category : values()) {
            if (category.code.equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid AdCategory code: " + code);
    }

    public static AdGroupEnum fromCodeAndIsSell(String code, boolean isSell) {
        if (code == null) return null;
        AdGroupEnum adGroupEnum = Arrays.stream(values())
                .filter(f -> f.code.equals(code))
                .findAny()
                .orElse(null);


        if (adGroupEnum == null) {
            String finalCode = concatIsSell(code, isSell);
            adGroupEnum = Arrays.stream(values())
                    .filter(f -> f.code.equals(finalCode))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid AdGroupEnum code: " + finalCode));
        }

        return adGroupEnum;
    }

    private static String concatIsSell(String codeParam, boolean isSell) {
        if (isSell) return codeParam + "-sell";
        else return codeParam + "-rent";
    }
}
