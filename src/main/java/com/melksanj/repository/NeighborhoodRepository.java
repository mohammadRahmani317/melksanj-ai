package com.melksanj.repository;

import com.melksanj.model.Neighborhood;
import com.melksanj.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
    Optional<Neighborhood> findByNameAndCity(String neighborhoodName, City city);
}
