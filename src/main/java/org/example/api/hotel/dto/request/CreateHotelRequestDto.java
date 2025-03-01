package org.example.api.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHotelRequestDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 100)
    private String brand;

    @NotNull
    private AddressRequestDto address;

    @NotNull
    private ContactRequestDto contacts;

    @NotNull
    private ArrivalTimeRequestDto arrivalTime;
}