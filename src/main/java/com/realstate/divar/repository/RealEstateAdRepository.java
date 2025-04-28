package com.realstate.divar.repository;


import com.realstate.divar.model.RealEstateAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateAdRepository extends JpaRepository<RealEstateAd, Long> {
    List<RealEstateAd> findByCitySlugAndPriceValueIsNotNull(String citySlug);








}
