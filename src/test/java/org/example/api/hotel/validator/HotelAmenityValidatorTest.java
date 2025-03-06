package org.example.api.hotel.validator;

import org.example.api.hotel.exception.AmenityAlreadyExistsException;
import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.example.api.hotel.repository.AmenityRepository;
import org.example.api.hotel.repository.HotelAmenityRepository;
import org.example.api.hotel.util.TestDataBuilderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelAmenityValidatorTest {

    @InjectMocks
    private HotelAmenityValidator hotelAmenityValidator;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelAmenityRepository hotelAmenityRepository;

    private Hotel hotel;
    private List<String> amenityNames;
    private List<Amenity> existingAmenities;
    private List<HotelAmenity> existingHotelAmenities;

    @BeforeEach
    public void setUp() {
        hotel = TestDataBuilderUtil.generateHotel();
        existingAmenities = TestDataBuilderUtil.generateAmenities();
        amenityNames = existingAmenities.stream()
                .map(Amenity::getName)
                .toList();
        existingHotelAmenities = existingAmenities.stream()
                .map(amenity -> HotelAmenity.builder()
                        .hotel(hotel)
                        .amenity(amenity)
                        .build())
                .collect(Collectors.toList());
    }

    @Test
    public void throwExceptionWhenAllAmenitiesAlreadyExist() {
        when(amenityRepository.findByNameIn(amenityNames)).thenReturn(existingAmenities);
        when(hotelAmenityRepository.findByHotelAndAmenityIn(hotel, existingAmenities))
                .thenReturn(existingHotelAmenities);

        var exception = assertThrows(AmenityAlreadyExistsException.class, () ->
                hotelAmenityValidator.validateNewAmenitiesForHotel(hotel, amenityNames));

        assertThat(exception.getMessage()).contains(hotel.getName());
        assertThat(exception.getMessage()).contains(amenityNames);

        verify(amenityRepository).findByNameIn(amenityNames);
        verify(hotelAmenityRepository).findByHotelAndAmenityIn(hotel, existingAmenities);
    }

    @Test
    public void returnExistingAmenitiesWhenSomeAreNew() {
        when(amenityRepository.findByNameIn(amenityNames)).thenReturn(existingAmenities);
        when(hotelAmenityRepository.findByHotelAndAmenityIn(hotel, existingAmenities))
                .thenReturn(List.of(existingHotelAmenities.get(0)));

        List<Amenity> validatedAmenities = hotelAmenityValidator.validateNewAmenitiesForHotel(hotel, amenityNames);

        assertThat(validatedAmenities).hasSize(existingAmenities.size());
        assertThat(validatedAmenities).containsAll(existingAmenities);

        verify(amenityRepository).findByNameIn(amenityNames);
        verify(hotelAmenityRepository).findByHotelAndAmenityIn(hotel, existingAmenities);
    }
}