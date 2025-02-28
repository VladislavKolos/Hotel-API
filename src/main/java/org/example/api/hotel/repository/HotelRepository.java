package org.example.api.hotel.repository;

import org.example.api.hotel.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {

    @Query("SELECT DISTINCT h FROM Hotel h")
    @EntityGraph(attributePaths = {"contact"})
    Page<Hotel> findAllHotelsWithContactPageable(Pageable pageable);

    @EntityGraph(attributePaths = {"contact"})
    @Query("""
                SELECT DISTINCT h FROM Hotel h
                WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))
                  AND (:brand IS NULL OR LOWER(h.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
                  AND (:city IS NULL OR LOWER(h.address.city) LIKE LOWER(CONCAT('%', :city, '%')))
                  AND (:country IS NULL OR LOWER(h.address.country) LIKE LOWER(CONCAT('%', :country, '%')))
            """)
    Page<Hotel> findHotelsByFilters(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("city") String city,
            @Param("country") String country,
            Pageable pageable
    );
}
