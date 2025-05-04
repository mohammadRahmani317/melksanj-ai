package com.realstate.divar.repository;

import com.realstate.divar.model.City;
import com.realstate.divar.model.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
    Optional<Neighborhood> findByNameAndCity(String neighborhoodName, City city);
}
