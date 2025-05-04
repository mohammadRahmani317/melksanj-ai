package com.realstate.divar.repository;

import com.realstate.divar.model.AdGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdGroupRepository extends JpaRepository<AdGroup, Long> {
    Optional<AdGroup> findByCode(String code);
}
