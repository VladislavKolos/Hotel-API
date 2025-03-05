package org.example.api.hotel.repository;

import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.example.api.hotel.util.TestDataBuilderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class HotelAmenityRepositoryTest {

    @Autowired
    private HotelAmenityRepository hotelAmenityRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ContactRepository contactRepository;

    private Hotel hotel;
    private List<Amenity> amenities;

    @BeforeEach
    public void setUp() {
        hotel = TestDataBuilderUtil.generateHotelWithRelations(contactRepository);
        hotelRepository.save(hotel);

        amenities = TestDataBuilderUtil.generateAmenities();
        amenityRepository.saveAll(amenities);
        List<HotelAmenity> hotelAmenities = amenities.stream()
                .map(amenity -> HotelAmenity.builder().hotel(hotel).amenity(amenity).build())
                .collect(Collectors.toList());

        hotelAmenityRepository.saveAll(hotelAmenities);
    }

    @AfterEach
    public void cleanUp() {
        hotelAmenityRepository.deleteAll();
        amenityRepository.deleteAll();
        contactRepository.deleteAll();
        hotelRepository.deleteAll();
    }

    @Test
    public void testFindHotelsByAmenityFilterSuccess() {
        String amenityName = amenities.get(0).getName();

        Page<Hotel> hotels = hotelAmenityRepository.findHotelsByAmenityFilter(amenityName, PageRequest.of(0, 10));

        assertThat(hotels).isNotEmpty();
        assertThat(hotels.getContent()).containsExactly(hotel);
    }

    @Test
    public void testFindAllHotelAmenitiesSuccess() {
        List<HotelAmenity> hotelAmenities = hotelAmenityRepository.findAllHotelAmenities();

        assertThat(hotelAmenities).hasSize(2);
        assertThat(hotelAmenities.stream().map(HotelAmenity::getAmenity).toList()).containsAll(amenities);
    }

    @Test
    public void testFindByHotelAndAmenityInSuccess() {
        List<HotelAmenity> foundAmenities = hotelAmenityRepository.findByHotelAndAmenityIn(hotel, amenities);

        assertThat(foundAmenities).hasSize(2);
        assertThat(foundAmenities.stream().map(HotelAmenity::getAmenity).toList()).containsAll(amenities);
    }
}