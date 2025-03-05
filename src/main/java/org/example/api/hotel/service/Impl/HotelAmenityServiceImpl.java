package org.example.api.hotel.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.exception.EntityHotelNotFoundException;
import org.example.api.hotel.mapper.HotelMapper;
import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.example.api.hotel.repository.AmenityRepository;
import org.example.api.hotel.repository.HotelAmenityRepository;
import org.example.api.hotel.repository.HotelRepository;
import org.example.api.hotel.service.HotelAmenityService;
import org.example.api.hotel.validator.HotelAmenityValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HotelAmenityServiceImpl implements HotelAmenityService {
    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelAmenityValidator hotelAmenityValidator;
    private final HotelAmenityRepository hotelAmenityRepository;

    @Override
    @Transactional
    public FullHotelInfoResponseDto addAmenitiesToHotel(UUID hotelId, List<String> request) {
        var hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityHotelNotFoundException("Hotel with ID " + hotelId + " not found"));

        List<String> uniqueAmenityNames = request.stream().distinct().toList();
        List<Amenity> existingAmenities = hotelAmenityValidator.validateNewAmenitiesForHotel(hotel, uniqueAmenityNames);
        List<Amenity> amenities = findOrCreateAmenities(uniqueAmenityNames, existingAmenities);

        linkAmenitiesToHotel(hotel, amenities);

        return hotelMapper.toFullHotelInfoResponseDto(hotel);
    }

    private List<Amenity> findOrCreateAmenities(List<String> amenityNames, List<Amenity> existingAmenities) {
        List<String> existingNames = existingAmenities.stream()
                .map(Amenity::getName)
                .toList();

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
        List<String> existingAmenityNames = hotel.getHotelAmenities().stream()
                .map(ha -> ha.getAmenity().getName())
                .toList();

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