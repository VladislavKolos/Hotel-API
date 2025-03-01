package org.example.api.hotel.dto.response;

public record AddressResponseDto(Integer houseNumber,
                                 String street,
                                 String city,
                                 String country,
                                 String postCode) {
}