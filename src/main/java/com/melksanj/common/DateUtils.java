package com.melksanj.common;

import com.github.mfathi91.time.PersianDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {


    /**
     * Converts a Gregorian year to a Persian year key (e.g., "1402")
     */
    public static String toPersianYear(int gregorianYear) {
        LocalDate date = LocalDate.of(gregorianYear, 1, 1);
        return String.valueOf(PersianDate.fromGregorian(date).getYear());
    }


    /**
     * Converts a Gregorian year and month to a Persian "year/month" key (e.g., "1402/5")
     */
    public static String toPersianYearMonth(int gregorianYear, int gregorianMonth) {
        LocalDate date = LocalDate.of(gregorianYear, gregorianMonth, 1);
        PersianDate persianDate = PersianDate.fromGregorian(date);
        return persianDate.getYear() + "/" + persianDate.getMonthValue();
    }
}
