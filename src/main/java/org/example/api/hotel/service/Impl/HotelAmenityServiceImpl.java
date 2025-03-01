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
import java.util.UUID;
import java.util.stream.Collectors;

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

        List<Amenity> amenities = createAmenities(request);

        linkAmenitiesToHotel(hotel, amenities);

        return hotelMapper.toFullHotelInfoResponseDto(hotel);
    }

    private List<Amenity> createAmenities(List<String> amenityNames) {
        List<Amenity> amenities = amenityNames.stream()
                .map(name -> Amenity.builder().name(name).build())
                .collect(Collectors.toList());

        return amenityRepository.saveAll(amenities);
    }

    private void linkAmenitiesToHotel(Hotel hotel, List<Amenity> amenities) {
        List<HotelAmenity> hotelAmenities = amenities.stream()
                .map(amenity -> HotelAmenity.builder().hotel(hotel).amenity(amenity).build())
                .collect(Collectors.toList());

        hotelAmenityRepository.saveAll(hotelAmenities);

        hotel.getHotelAmenities().addAll(hotelAmenities);
    }
}
