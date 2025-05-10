package com.melksanj.repository;

import com.melksanj.model.AdCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdCategoryRepository extends JpaRepository<AdCategory,Long> {
    Optional<AdCategory> findByCode(String code);
}
