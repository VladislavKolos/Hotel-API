package org.example.api.hotel.dto.response;

import java.util.List;
import java.util.UUID;

public record FullHotelInfoResponseDto(UUID id,
                                       String name,
                                       String description,
                                       String brand,
                                       AddressResponseDto address,
                                       ContactResponseDto contacts,
                                       ArrivalTimeResponseDto arrivalTime,
                                       List<String> amenities) {
}
