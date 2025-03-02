package org.example.api.hotel.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchRequestDto {

    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String brand;

    @Size(max = 163)
    private String city;

    @Size(max = 56)
    private String country;

    private List<String> amenities;
}