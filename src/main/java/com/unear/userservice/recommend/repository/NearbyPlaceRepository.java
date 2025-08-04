package com.unear.userservice.recommend.repository;

import com.unear.userservice.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NearbyPlaceRepository extends JpaRepository<Place, Long> {

    @Query(value = """
        SELECT *
        FROM places
        WHERE ST_DistanceSphere(
                 ST_MakePoint(:lng, :lat),
                 ST_MakePoint(longitude, latitude)
              ) <= :radius
        ORDER BY ST_DistanceSphere(
                 ST_MakePoint(:lng, :lat),
                 ST_MakePoint(longitude, latitude)
              )
    """, nativeQuery = true)
    List<Place> findNearbyPlaces(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radius
    );
}
