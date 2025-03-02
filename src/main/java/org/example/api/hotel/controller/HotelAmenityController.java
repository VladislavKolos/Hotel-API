package org.example.api.hotel.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.service.HotelAmenityService;
import org.example.api.hotel.util.uri.UriUtil;
import org.example.api.hotel.util.uri.enums.UriType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelAmenityController {
    private final HotelAmenityService hotelAmenityService;

    @PostMapping("/{id}/amenities")
    public ResponseEntity<FullHotelInfoResponseDto> addAmenitiesToHotel(@PathVariable UUID id,
                                                                        @RequestBody @Size(min = 1) List<@NotBlank String> request) {
        log.info("Incoming request to add amenities to hotel with details: {}", request);

        var response = hotelAmenityService.addAmenitiesToHotel(id, request);
        log.info("Amenities added to hotel with ID: {} successfully", response.id());

        String resourceUri = UriUtil.createUri(UriType.HOTEL_AMENITY, response.id());
        log.info("Hotel with amenities resource URI created: {}", resourceUri);

        return ResponseEntity.created(URI.create(resourceUri))
                .body(response);
    }
}