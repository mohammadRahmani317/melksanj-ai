package com.realstate.divar.repository;


import com.realstate.divar.model.RealEstateAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateAdRepository extends JpaRepository<RealEstateAd, Long> {

    @Query("""
                select r from RealEstateAd r
                where r.city.id = :cityId
                and (:groupCode is null or r.adGroup.code = :groupCode)
                and (:categoryCode is null or r.adCategory.code = :categoryCode)
                and r.priceValue is not null
            """)
    List<RealEstateAd> findByCityAndAdGroupAndAdCategoryAndPriceValueIsNotNull(Long cityId, String groupCode, String categoryCode);
}
