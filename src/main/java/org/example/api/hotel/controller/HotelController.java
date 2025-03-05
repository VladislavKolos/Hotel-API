package org.example.api.hotel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.request.HotelSearchRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.model.enums.HotelGroupingParameter;
import org.example.api.hotel.service.HotelService;
import org.example.api.hotel.util.uri.UriUtil;
import org.example.api.hotel.util.uri.enums.UriType;
import org.example.api.hotel.validator.constraint.annotation.ExistingHotel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @GetMapping("/hotels")
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

    @PostMapping("/hotels")
    public ResponseEntity<ShortHotelInfoResponseDto> createHotel(@Valid @RequestBody CreateHotelRequestDto request) {
        log.info("Incoming request to create hotel with details: {}", request);

        var response = hotelService.createHotel(request);
        log.info("Hotel created successfully with ID: {}", response.id());

        String resourceUri = UriUtil.createUri(UriType.HOTEL, response.id());
        log.info("Hotel resource URI created: {}", resourceUri);

        return ResponseEntity.created(URI.create(resourceUri))
                .body(response);
    }

    @GetMapping("/hotels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FullHotelInfoResponseDto getHotelById(@PathVariable @ExistingHotel UUID id) {
        log.info("Incoming request to retrieve hotel with ID: {}", id);

        var response = hotelService.getHotelById(id);
        log.info("Retrieved hotel with ID: {}", id);

        return response;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<ShortHotelInfoResponseDto> searchHotels(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String brand,
                                                        @RequestParam(required = false) String city,
                                                        @RequestParam(required = false) String country,
                                                        @RequestParam(required = false) List<String> amenities,
                                                        @PageableDefault(sort = "name", direction =
                                                                Sort.Direction.ASC) Pageable pageable) {
        log.info("""
                         Incoming request to search hotels with filters: name={}, brand={}, city={}, country={},\s
                         amenities={}, pageable={}
                        \s""",
                name, brand, city, country, amenities, pageable);

        var request = buildHotelSearchRequestDto(name, brand, city, country, amenities);
        var response = hotelService.searchHotels(request, pageable);
        log.info("Returning {} hotels for the search request.", response.getTotalElements());

        return response;
    }

    @GetMapping("/histogram/{param}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> countHotelsGroupedByParameter(@PathVariable String param) {
        log.info("Incoming request to get hotel count grouped by parameter: {}", param);

        var groupingParameter = HotelGroupingParameter.parseOrThrow(param);
        var response = hotelService.countHotelsGroupedByParameter(groupingParameter);
        log.info("Returning histogram for parameter '{}' with {} values.", groupingParameter, response.size());

        return response;
    }

    private HotelSearchRequestDto buildHotelSearchRequestDto(String name, String brand, String city, String country,
                                                             List<String> amenities) {
        return HotelSearchRequestDto.builder()
                .name(name)
                .brand(brand)
                .city(city)
                .country(country)
                .amenities(amenities)
                .build();
    }
}