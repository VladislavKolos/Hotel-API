package org.example.api.hotel.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.hotel.repository.HotelRepository;
import org.example.api.hotel.validator.constraint.annotation.ExistingHotel;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExistingHotelValidator implements ConstraintValidator<ExistingHotel, UUID> {
    private final HotelRepository hotelRepository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {
        boolean exists = hotelRepository.existsById(id);

        if (!exists) {
            log.info("Validation failed: Hotel with ID {} does not exist", id);
        }
        return exists;
    }
}