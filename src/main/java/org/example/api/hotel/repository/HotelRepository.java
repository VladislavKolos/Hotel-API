package org.example.api.hotel.repository;

import org.example.api.hotel.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {

    @EntityGraph(attributePaths = {"contact"})
    Page<Hotel> findAllHotelsWithContactPageable(Pageable pageable);

    @EntityGraph(attributePaths = {"contact"})
    Page<Hotel> findAllWithSpecification(Specification<Hotel> spec, Pageable pageable);
}
