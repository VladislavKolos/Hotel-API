package org.example.api.hotel.repository;

import org.example.api.hotel.dto.request.HotelFilterRequestDto;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.repository.specification.HotelSpecification;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class HotelRepositoryTest {

    @Autowired
    private HotelAmenityRepository hotelAmenityRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    public void setUp() {
        var hotel = TestDataBuilderUtil.generateHotelWithRelations(contactRepository);
        hotelRepository.save(hotel);
    }

    @AfterEach
    public void cleanUp() {
        hotelAmenityRepository.deleteAll();
        amenityRepository.deleteAll();
        contactRepository.deleteAll();
        hotelRepository.deleteAll();
    }

    @Test
    public void testFindAllHotelsWithContactPageableSuccess() {
        Page<Hotel> hotels = hotelRepository.findAllHotelsWithContactPageable(PageRequest.of(0, 10));
        assertThat(hotels).isNotEmpty();
    }

    @Test
    public void testFindAllBrandsSuccess() {
        List<String> brands = hotelRepository.findAllBrands();
        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testFindAllCitiesSuccess() {
        List<String> cities = hotelRepository.findAllCities();
        assertThat(cities).isNotEmpty();
    }

    @Test
    public void testFindAllCountriesSuccess() {
        List<String> countries = hotelRepository.findAllCountries();
        assertThat(countries).isNotEmpty();
    }

    @Test
    public void testFindAllWithSpecificationSuccess() {
        var hotel = hotelRepository.findAll().get(0);
        var filter = builderHotelFilterRequestDto(hotel.getName(), hotel.getBrand(),
                hotel.getAddress().getCity(), hotel.getAddress().getCountry());

        Page<Hotel> hotels = hotelRepository.findAll(
                HotelSpecification.filterBy(filter),
                PageRequest.of(0, 10)
        );

        assertThat(hotels).isNotEmpty();
        assertThat(hotels.getContent()).containsExactly(hotel);
    }

    private HotelFilterRequestDto builderHotelFilterRequestDto(String name, String brand, String city, String country) {
        return HotelFilterRequestDto.builder()
                .name(name)
                .brand(brand)
                .city(city)
                .country(country)
                .build();
    }
}