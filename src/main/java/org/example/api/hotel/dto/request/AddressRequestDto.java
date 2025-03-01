package org.example.api.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {

    @NotNull
    private Integer houseNumber;

    @NotBlank
    @Size(max = 85)
    private String street;

    @NotBlank
    @Size(max = 163)
    private String city;

    @NotBlank
    @Size(max = 56)
    private String country;

    @NotBlank
    @Pattern(regexp = "\\d{4,10}")
    private String postCode;
}