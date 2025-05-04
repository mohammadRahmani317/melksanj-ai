package com.realstate.divar.repository;

import com.realstate.divar.model.AdCategory;
import com.realstate.divar.model.AdGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdCategoryRepository extends JpaRepository<AdCategory,Long> {
    Optional<AdCategory> findByCode(String code);
}
