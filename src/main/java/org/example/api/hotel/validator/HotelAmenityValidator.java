package org.example.api.hotel.validator;

import lombok.RequiredArgsConstructor;
import org.example.api.hotel.exception.AmenityAlreadyExistsException;
import org.example.api.hotel.model.Amenity;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.example.api.hotel.repository.AmenityRepository;
import org.example.api.hotel.repository.HotelAmenityRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HotelAmenityValidator {
    private final AmenityRepository amenityRepository;
    private final HotelAmenityRepository hotelAmenityRepository;

    public List<Amenity> validateNewAmenitiesForHotel(Hotel hotel, List<String> amenityNames) {
        List<Amenity> existingAmenities = amenityRepository.findByNameIn(amenityNames);
        List<HotelAmenity> existingHotelAmenities = hotelAmenityRepository.findByHotelAndAmenityIn(hotel,
                existingAmenities);

        if (existingHotelAmenities.size() == amenityNames.size()) {
            throw new AmenityAlreadyExistsException(hotel.getName(), amenityNames);
        }
        return existingAmenities;
    }
}