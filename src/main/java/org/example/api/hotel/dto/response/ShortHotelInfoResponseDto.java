package org.example.api.hotel.dto.response;

import java.util.UUID;

public record ShortHotelInfoResponseDto(UUID id,
                                        String name,
                                        String description,
                                        String address,
                                        String phone) {
}