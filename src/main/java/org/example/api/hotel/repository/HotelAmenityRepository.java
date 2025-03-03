package org.example.api.hotel.repository;

import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelAmenityRepository extends JpaRepository<HotelAmenity, UUID> {

    @EntityGraph(attributePaths = {"hotel", "amenity"})
    @Query("""
                SELECT DISTINCT ha.hotel FROM HotelAmenity ha
                WHERE LOWER(ha.amenity.name) LIKE LOWER(CONCAT('%', :amenity, '%'))
            """)
    Page<Hotel> findHotelsByAmenityFilter(@Param("amenity") String amenity, Pageable pageable);

    @EntityGraph(attributePaths = {"hotel", "amenity"})
    @Query("SELECT ha FROM HotelAmenity ha")
    List<HotelAmenity> findAllHotelAmenities();

    @EntityGraph(attributePaths = {"hotel", "amenity"})
    @Query("SELECT ha FROM HotelAmenity ha WHERE ha.hotel = :hotel AND ha.amenity IN :amenities")
    List<HotelAmenity> findByHotelAndAmenityIn(@Param("hotel") Hotel hotel, @Param("amenities") List<Amenity> amenities);
}