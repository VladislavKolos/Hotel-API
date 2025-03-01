package org.example.api.hotel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.service.HotelService;
import org.example.api.hotel.service.UriService;
import org.example.api.hotel.service.enums.UriType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final UriService uriService;
    private final HotelService hotelService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ShortHotelInfoResponseDto> getAllHotelsWithShortInfo(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Incoming request to retrieve all hotels with short information. Pageable: {}", pageable);

        var response = hotelService.getAllHotelsWithShortInfo(pageable);
        log.info("Retrieved all hotels with short information. Page number: {}, page size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());

        return response;
    }

    @PostMapping
    public ResponseEntity<ShortHotelInfoResponseDto> createHotel(@Valid @RequestBody CreateHotelRequestDto request) {
        log.info("Incoming request to create hotel with details: {}", request);

        var response = hotelService.createHotel(request);
        log.info("Hotel created successfully with ID: {}", response.id());

        String resourceUri = uriService.createUri(UriType.HOTEL, response.id());
        log.info("Hotel resource URI created: {}", resourceUri);

        return ResponseEntity.created(URI.create(resourceUri))
                .body(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FullHotelInfoResponseDto getHotelById(@PathVariable UUID id) {
        log.info("Incoming request to retrieve hotel with ID: {}", id);

        var response = hotelService.getHotelById(id);
        log.info("Retrieved hotel with ID: {}", id);

        return response;
    }
}