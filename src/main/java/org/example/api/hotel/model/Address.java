package org.example.api.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Embeddable
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotNull
    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;

    @NotBlank
    @Size(max = 85)
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank
    @Size(max = 163)
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank
    @Size(max = 56)
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank
    @Pattern(regexp = "\\d{4,10}")
    @Column(name = "post_code", nullable = false)
    private String postCode;
}
