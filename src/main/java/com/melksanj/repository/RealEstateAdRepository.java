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
                AND r.priceValue IS NOT NULL AND r.priceValue >10000
                AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
                AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
                AND (:region IS NULL OR r.neighborhood.region = :region)
                GROUP BY FUNCTION('YEAR', r.createdAtMonth)
                ORDER BY FUNCTION('YEAR', r.createdAtMonth)
            """)
    List<Object[]> findYearlyAverageSalePrices(@Param("cityId") Long cityId,
                                               @Param("groupCodes") List<String> groupCodes,
                                               @Param("categoryCodes") List<String> categoryCodes,
                                               @Param("region") Integer region
    );

    @Query("SELECT DISTINCT YEAR(r.createdAtMonth) FROM RealEstateAd r ORDER BY YEAR(r.createdAtMonth)")
    List<Integer> findDistinctYears();

    @Query("""
             SELECT FUNCTION('MONTH', r.createdAtMonth), AVG(r.priceValue)
             FROM RealEstateAd r
             WHERE r.city.id = :cityId
             AND r.priceValue IS NOT NULL AND r.priceValue >10000
             AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
             AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
             AND (:region IS NULL OR r.neighborhood.region = :region)
             AND FUNCTION('YEAR', r.createdAtMonth) = :year
             GROUP BY FUNCTION('MONTH', r.createdAtMonth)
             ORDER BY FUNCTION('MONTH', r.createdAtMonth)
            """)
    List<Object[]> findMonthlyAverageSalePrices(@Param("cityId") Long cityId,
                                                @Param("groupCodes") List<String> groupCodes,
                                                @Param("categoryCodes") List<String> categoryCodes,
                                                @Param("year") int year,
                                                @Param("region") Integer region);

    @Query("""
                SELECT FUNCTION('YEAR', r.createdAtMonth) as year,
                       AVG(r.creditValue) as credit,
                       AVG(r.rentValue) as rent
                FROM RealEstateAd r
                WHERE r.city.id = :cityId
                  AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
                  AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
                  AND (:region IS NULL OR r.neighborhood.region = :region)
                GROUP BY FUNCTION('YEAR', r.createdAtMonth)
                ORDER BY FUNCTION('YEAR', r.createdAtMonth)
            """)
    List<Object[]> findYearlyAverageRentAndCredit(@Param("cityId") Long cityId,
                                                  @Param("groupCodes") List<String> groupCodes,
                                                  @Param("categoryCodes") List<String> categoryCodes,
                                                  @Param("region") Integer region);

    @Query("""
             SELECT FUNCTION('MONTH', r.createdAtMonth) as month,
                    AVG(r.creditValue) as credit,
                    AVG(r.rentValue) as rent
             FROM RealEstateAd r
             WHERE r.city.id = :cityId
               AND FUNCTION('YEAR', r.createdAtMonth) = :year
               AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
               AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
               AND (:region IS NULL OR r.neighborhood.region = :region)
             GROUP BY FUNCTION('MONTH', r.createdAtMonth)
             ORDER BY FUNCTION('MONTH', r.createdAtMonth)
            """)
    List<Object[]> findMonthlyAverageRentAndCredit(@Param("cityId") Long cityId,
                                                   @Param("year") Integer year,
                                                   @Param("groupCodes") List<String> groupCodes,
                                                   @Param("categoryCodes") List<String> categoryCodes,
                                                   @Param("region") Integer region
    );


    @Query("""
                        SELECT FUNCTION('YEAR', r.createdAtMonth), AVG(r.priceValue / r.buildingSize)
                                    FROM RealEstateAd r
                                    WHERE r.city.id = :cityId
                                      AND r.priceValue IS NOT NULL AND r.priceValue >10000
                                      AND r.buildingSize IS NOT NULL AND r.buildingSize > 10
                                      AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
                                      AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
                                      AND (:region IS NULL OR r.neighborhood.region = :region)
                                    GROUP BY FUNCTION('YEAR', r.createdAtMonth)
                                    ORDER BY FUNCTION('YEAR', r.createdAtMonth)
            """)
    List<Object[]> findYearlyAveragePricePerSquareMeter(@Param("cityId") Long cityId,
                                                        @Param("groupCodes") List<String> groupCodes,
                                                        @Param("categoryCodes") List<String> categoryCodes,
                                                        @Param("region") Integer region
    );


    @Query("""
                SELECT FUNCTION('MONTH', r.createdAtMonth), AVG(r.priceValue / r.buildingSize)
                FROM RealEstateAd r
                WHERE r.city.id = :cityId
                  AND FUNCTION('YEAR', r.createdAtMonth) = :year
                  AND r.priceValue IS NOT NULL AND r.priceValue >10000
                  AND r.buildingSize IS NOT NULL AND r.buildingSize > 10
                  AND (:groupCodes IS NULL OR r.adGroup.code IN :groupCodes)
                  AND (:categoryCodes IS NULL OR r.adCategory.code IN :categoryCodes)
                  AND (:region IS NULL OR r.neighborhood.region = :region)
                GROUP BY FUNCTION('MONTH', r.createdAtMonth)
                ORDER BY FUNCTION('MONTH', r.createdAtMonth)
            """)
    List<Object[]> findMonthlyAveragePricePerSquareMeter(@Param("cityId") Long cityId,
                                                         @Param("groupCodes") List<String> groupCodes,
                                                         @Param("categoryCodes") List<String> categoryCodes,
                                                         @Param("year") int year,
                                                         @Param("region") Integer region
    );

    @Query("""
    SELECT n.nameFa, FUNCTION('YEAR', a.createdAtMonth), AVG(a.priceValue / a.buildingSize)
    FROM RealEstateAd a
    JOIN a.neighborhood n
    WHERE a.city.id = :cityId
      AND a.priceValue > 0 AND a.buildingSize > 0
      AND (:groupCode IS NULL OR a.adGroup = :groupCode)
      AND (:categoryCode IS NULL OR a.adCategory = :categoryCode)
    GROUP BY n.nameFa, FUNCTION('YEAR', a.createdAtMonth)
    ORDER BY n.nameFa, FUNCTION('YEAR', a.createdAtMonth)
""")
    List<Object[]> getYearlyAveragePricePerRegion(@Param("cityId") Long cityId,
                                                  @Param("groupCode") String groupCode,
                                                  @Param("categoryCode") String categoryCode);

}
