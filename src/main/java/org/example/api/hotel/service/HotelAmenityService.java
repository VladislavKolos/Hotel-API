package org.example.api.hotel.service;

import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;

import java.util.List;
import java.util.UUID;

public interface HotelAmenityService {
    FullHotelInfoResponseDto addAmenitiesToHotel(UUID hotelId, List<String> request);
}
