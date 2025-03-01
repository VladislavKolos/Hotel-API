package org.example.api.hotel.repository;

import org.example.api.hotel.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {

    @Query("SELECT DISTINCT h FROM Hotel h")
    @EntityGraph(attributePaths = {"contact"})
    Page<Hotel> findAllHotelsWithContactPageable(Pageable pageable);

    @Query("SELECT h.brand FROM Hotel h")
    List<String> findAllBrands();

    @Query("SELECT h.address.city FROM Hotel h")
    List<String> findAllCities();

    @Query("SELECT h.address.country FROM Hotel h")
    List<String> findAllCountries();

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"contact"})
    Page<Hotel> findAll(@NonNull Specification<Hotel> spec, @NonNull Pageable pageable);
}
