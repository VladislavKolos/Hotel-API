package org.example.api.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Getter
@Setter
@Embeddable
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {

    @NotNull
    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private LocalTime checkOut;
}
