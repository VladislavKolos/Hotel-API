package org.example.api.hotel.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.mapper.HotelMapper;
import org.example.api.hotel.repository.ContactRepository;
import org.example.api.hotel.repository.HotelRepository;
import org.example.api.hotel.service.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;
    private final ContactRepository contactRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ShortHotelInfoResponseDto> getAllHotelsWithShortInfo(Pageable pageable) {
        return hotelRepository.findAllHotelsWithContactPageable(pageable)
                .map(hotelMapper::toShortHotelInfoResponseDto);
    }

    @Override
    @Transactional
    public ShortHotelInfoResponseDto createHotel(CreateHotelRequestDto request) {
        return Optional.of(request)
                .map(hotelMapper::toEntity)
                .map(hotel -> {
                    contactRepository.save(hotel.getContact());
                    return hotelRepository.save(hotel);
                })
                .map(hotelMapper::toShortHotelInfoResponseDto)
                .orElseThrow(() -> new IllegalStateException(
                        "Error saving hotel")); //TODO Later I will create custom exceptions
    }

    @Override
    @Transactional(readOnly = true)
    public FullHotelInfoResponseDto getHotelById(UUID id) {
        return hotelRepository.findById(id)
                .map(hotelMapper::toFullHotelInfoResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hotel not found")); //TODO Later I will create custom exceptions
    }
}