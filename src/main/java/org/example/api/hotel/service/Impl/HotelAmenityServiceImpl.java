package org.example.api.hotel.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.mapper.HotelMapper;
import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.example.api.hotel.repository.AmenityRepository;
import org.example.api.hotel.repository.HotelAmenityRepository;
import org.example.api.hotel.repository.HotelRepository;
import org.example.api.hotel.service.HotelAmenityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HotelAmenityServiceImpl implements HotelAmenityService {
    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelAmenityRepository hotelAmenityRepository;

    @Override
    @Transactional
    public FullHotelInfoResponseDto addAmenitiesToHotel(UUID hotelId, List<String> request) {
        var hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("")); //TODO Later I will create custom exceptions

        List<Amenity> amenities = findOrCreateAmenities(request);

        linkAmenitiesToHotel(hotel, amenities);

        return hotelMapper.toFullHotelInfoResponseDto(hotel);
    }

    private List<Amenity> findOrCreateAmenities(List<String> amenityNames) {
        List<Amenity> existingAmenities = amenityRepository.findByNameIn(amenityNames);

        Set<String> existingNames = existingAmenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());

        List<Amenity> newAmenities = amenityNames.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> Amenity.builder().name(name).build())
                .collect(Collectors.toList());

        return newAmenities.isEmpty() ?
                existingAmenities
                :
                Stream.concat(existingAmenities.stream(), amenityRepository.saveAll(newAmenities).stream())
                        .toList();
    }

    private void linkAmenitiesToHotel(Hotel hotel, List<Amenity> amenities) {
        Set<String> existingAmenityNames = hotel.getHotelAmenities().stream()
                .map(ha -> ha.getAmenity().getName())
                .collect(Collectors.toSet());

        List<HotelAmenity> newHotelAmenities = amenities.stream()
                .filter(amenity -> !existingAmenityNames.contains(amenity.getName()))
                .map(amenity -> HotelAmenity.builder().hotel(hotel).amenity(amenity).build())
                .collect(Collectors.toList());

        if (!newHotelAmenities.isEmpty()) {
            hotelAmenityRepository.saveAll(newHotelAmenities);
            hotel.getHotelAmenities().addAll(newHotelAmenities);
        }
    }
}