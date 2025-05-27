package com.melksanj.repository;

import com.melksanj.model.Neighborhood;
import com.melksanj.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
    Optional<Neighborhood> findByNameAndCity(String neighborhoodName, City city);

    @Query("SELECT DISTINCT n.region FROM Neighborhood n WHERE n.city.id = :cityId AND n.region IS NOT NULL ORDER BY n.region ASC")
    List<Integer> findDistinctRegionsByCityId(@Param("cityId") Long cityId);
}
