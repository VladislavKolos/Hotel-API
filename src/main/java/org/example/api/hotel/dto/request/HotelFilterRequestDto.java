package org.example.api.hotel.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotelFilterRequestDto {

    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String brand;

    @Size(max = 163)
    private String city;

    @Size(max = 56)
    private String country;
}