package org.example.api.hotel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9. ()-]{10,18}$")
    private String phone;
}