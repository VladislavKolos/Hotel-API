package org.example.api.hotel.service;

import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HotelService {
    Page<ShortHotelInfoResponseDto> getAllHotelsWithShortInfo(Pageable pageable);

    ShortHotelInfoResponseDto createHotel(CreateHotelRequestDto request);

    FullHotelInfoResponseDto getHotelById(UUID id);
}