package org.example.api.hotel.service.Impl;

import org.example.api.hotel.exception.EntityHotelNotFoundException;
import org.example.api.hotel.mapper.HotelMapper;
import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.example.api.hotel.repository.AmenityRepository;
import org.example.api.hotel.repository.HotelAmenityRepository;
import org.example.api.hotel.repository.HotelRepository;
import org.example.api.hotel.util.TestDataBuilderUtil;
import org.example.api.hotel.validator.HotelAmenityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelAmenityServiceImplTest {

    @InjectMocks
    private HotelAmenityServiceImpl hotelAmenityService;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelAmenityValidator hotelAmenityValidator;

    @Mock
    private HotelAmenityRepository hotelAmenityRepository;

    private Hotel hotel;
    private List<String> amenityNames;

    @BeforeEach
    void setUp() {
        hotel = TestDataBuilderUtil.generateHotel();
        amenityNames = TestDataBuilderUtil.generateAmenities().stream()
                .map(Amenity::getName)
                .collect(Collectors.toList());
    }

    @Test
    void testAddAmenitiesToHotelSuccess() {
        var hotelId = hotel.getId();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        List<Amenity> existingAmenities = Collections.emptyList();
        when(hotelAmenityValidator.validateNewAmenitiesForHotel(hotel, amenityNames)).thenReturn(existingAmenities);

        List<Amenity> newAmenities = amenityNames.stream()
                .map(name -> Amenity.builder().name(name).build())
                .collect(Collectors.toList());
        when(amenityRepository.saveAll(newAmenities)).thenReturn(newAmenities);

        var responseDto = TestDataBuilderUtil.generateFullHotelInfoResponse();
        when(hotelMapper.toFullHotelInfoResponseDto(hotel)).thenReturn(responseDto);

        var response = hotelAmenityService.addAmenitiesToHotel(hotelId, amenityNames);

        assertNotNull(response);
        verify(hotelRepository).findById(hotelId);
        verify(amenityRepository).saveAll(newAmenities);
        verify(hotelAmenityRepository).saveAll(newAmenities.stream()
                .map(amenity -> HotelAmenity.builder().hotel(hotel).amenity(amenity).build())
                .collect(Collectors.toList()));
    }


    @Test
    void testAddAmenitiesToHotelWithExistingAmenities() {
        Amenity existingAmenity = TestDataBuilderUtil.generateAmenities().get(0);
        hotel.setHotelAmenities(new ArrayList<>(
                Collections.singletonList(HotelAmenity.builder().hotel(hotel).amenity(existingAmenity).build())));

        when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));

        List<Amenity> existingAmenities = Collections.singletonList(existingAmenity);
        when(hotelAmenityValidator.validateNewAmenitiesForHotel(hotel, amenityNames)).thenReturn(existingAmenities);

        List<Amenity> newAmenities = amenityNames.stream()
                .filter(name -> existingAmenities.stream().noneMatch(a -> a.getName().equals(name)))
                .map(name -> Amenity.builder().name(name).build())
                .collect(Collectors.toList());

        when(amenityRepository.saveAll(newAmenities)).thenReturn(newAmenities);

        var responseDto = TestDataBuilderUtil.generateFullHotelInfoResponse();
        when(hotelMapper.toFullHotelInfoResponseDto(hotel)).thenReturn(responseDto);

        var response = hotelAmenityService.addAmenitiesToHotel(hotel.getId(), amenityNames);

        assertNotNull(response);
        verify(hotelRepository).findById(hotel.getId());
        verify(amenityRepository).saveAll(newAmenities);
        verify(hotelAmenityRepository).saveAll(newAmenities.stream()
                .map(amenity -> HotelAmenity.builder().hotel(hotel).amenity(amenity).build())
                .collect(Collectors.toList()));
    }


    @Test
    void testAddAmenitiesToHotelWithNewAmenities() {
        Amenity existingAmenity = TestDataBuilderUtil.generateAmenities().get(0);
        hotel.setHotelAmenities(new ArrayList<>(
                Collections.singletonList(HotelAmenity.builder().hotel(hotel).amenity(existingAmenity).build())));

        List<String> newAmenities = TestDataBuilderUtil.generateAmenities().stream()
                .map(Amenity::getName)
                .collect(Collectors.toList());

        when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));

        List<Amenity> existingAmenities = Collections.singletonList(existingAmenity);
        when(hotelAmenityValidator.validateNewAmenitiesForHotel(hotel, newAmenities)).thenReturn(existingAmenities);

        List<Amenity> newAmenitiesList = newAmenities.stream()
                .map(name -> Amenity.builder().name(name).build())
                .collect(Collectors.toList());

        when(amenityRepository.saveAll(newAmenitiesList)).thenReturn(newAmenitiesList);

        var responseDto = TestDataBuilderUtil.generateFullHotelInfoResponse();
        when(hotelMapper.toFullHotelInfoResponseDto(hotel)).thenReturn(responseDto);

        var response = hotelAmenityService.addAmenitiesToHotel(hotel.getId(), newAmenities);

        assertNotNull(response);
        verify(hotelRepository).findById(hotel.getId());
        verify(amenityRepository).saveAll(newAmenitiesList);
        verify(hotelAmenityRepository).saveAll(newAmenitiesList.stream()
                .map(amenity -> HotelAmenity.builder().hotel(hotel).amenity(amenity).build())
                .collect(Collectors.toList()));
    }

    @Test
    void testAddAmenitiesToHotelHotelNotFound() {
        var hotelId = hotel.getId();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityHotelNotFoundException.class,
                () -> hotelAmenityService.addAmenitiesToHotel(hotelId, amenityNames));
        assertEquals("Hotel with ID " + hotelId + " not found", exception.getMessage());
    }
}