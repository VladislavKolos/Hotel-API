package org.example.api.hotel.repository;

import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.util.TestDataBuilderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AmenityRepositoryTest {

    @Autowired
    private AmenityRepository amenityRepository;

    private List<Amenity> amenities;

    @BeforeEach
    public void setUp() {
        amenities = TestDataBuilderUtil.generateAmenities();
    }

    @AfterEach
    public void cleanUp() {
        amenityRepository.deleteAll();
    }

    @Test
    public void testFindByNameInSuccess() {
        amenityRepository.saveAll(amenities);

        List<String> amenityNames = amenities.stream()
                .map(Amenity::getName)
                .toList();

        List<Amenity> foundAmenities = amenityRepository.findByNameIn(amenityNames);

        assertThat(foundAmenities).isNotEmpty();
        assertThat(foundAmenities).hasSize(amenities.size());
        assertThat(foundAmenities).extracting(Amenity::getName).containsAll(amenityNames);
    }

    @Test
    public void testFindByNameInEmptyResult() {
        List<String> amenityNames = amenities.stream()
                .map(Amenity::getName)
                .toList();

        List<Amenity> foundAmenities = amenityRepository.findByNameIn(amenityNames);

        assertThat(foundAmenities).isEmpty();
    }
}