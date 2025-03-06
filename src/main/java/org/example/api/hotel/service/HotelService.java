package org.example.api.hotel.service;

import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.request.SearchHotelRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.model.enums.HotelGroupingParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface HotelService {
    Page<ShortHotelInfoResponseDto> getAllHotelsWithShortInfo(Pageable pageable);

    ShortHotelInfoResponseDto createHotel(CreateHotelRequestDto request);

    FullHotelInfoResponseDto getHotelById(UUID id);

    Page<ShortHotelInfoResponseDto> searchHotels(SearchHotelRequestDto request, Pageable pageable);

    Map<String, Long> countHotelsGroupedByParameter(HotelGroupingParameter param);
}