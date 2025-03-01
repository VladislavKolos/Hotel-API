package org.example.api.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTimeRequestDto {

    @NotBlank
    private String checkIn;

    @NotBlank
    private String checkOut;
}
