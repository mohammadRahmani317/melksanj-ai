package com.melksanj.repository;


import com.melksanj.model.RealEstateAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateAdRepository extends JpaRepository<RealEstateAd, Long> {

    @Query("""
    SELECT FUNCTION('YEAR', r.createdAtMonth), AVG(r.priceValue)
    FROM RealEstateAd r
    WHERE r.city.id = :cityId
    AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
    AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
    GROUP BY FUNCTION('YEAR', r.createdAtMonth)
    ORDER BY FUNCTION('YEAR', r.createdAtMonth)
""")
    List<Object[]> findAveragePricePerYear(
            @Param("cityId") Long cityId,
            @Param("groupCodes") List<String> groupCodes,
            @Param("categoryCodes") List<String> categoryCodes
    );

    @Query("SELECT DISTINCT YEAR(r.createdAtMonth) FROM RealEstateAd r ORDER BY YEAR(r.createdAtMonth)")
    List<Integer> findDistinctYears();

    @Query("""
             SELECT FUNCTION('MONTH', r.createdAtMonth), AVG(r.priceValue)
             FROM RealEstateAd r
             WHERE r.city.id = :cityId
             AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
             AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
             AND FUNCTION('YEAR', r.createdAtMonth) = :year
             GROUP BY FUNCTION('MONTH', r.createdAtMonth)
             ORDER BY FUNCTION('MONTH', r.createdAtMonth)
            """)
    List<Object[]> findAveragePriceByMonthAndYear(@Param("cityId") Long cityId,
                                                  @Param("groupCodes") List<String> groupCodes,
                                                  @Param("categoryCodes") List<String> categoryCodes,
                                                  @Param("year") int year);

}
