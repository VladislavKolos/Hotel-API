package org.example.api.hotel.validator.constraint;

import jakarta.validation.ConstraintValidatorContext;
import org.example.api.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExistingHotelValidatorTest {

    @InjectMocks
    private ExistingHotelValidator existingHotelValidator;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private UUID validHotelId;
    private UUID invalidHotelId;

    @BeforeEach
    public void setUp() {
        validHotelId = UUID.randomUUID();
        invalidHotelId = UUID.randomUUID();
    }

    @Test
    public void testIsValidWhenHotelExistsReturnsTrue() {
        when(hotelRepository.existsById(validHotelId)).thenReturn(true);

        boolean isValid = existingHotelValidator.isValid(validHotelId, constraintValidatorContext);

        assertTrue(isValid);

        verify(hotelRepository).existsById(validHotelId);
    }

    @Test
    public void testIsValidWhenHotelDoesNotExistReturnsFalse() {
        when(hotelRepository.existsById(invalidHotelId)).thenReturn(false);

        boolean isValid = existingHotelValidator.isValid(invalidHotelId, constraintValidatorContext);

        assertFalse(isValid);

        verify(hotelRepository).existsById(invalidHotelId);
    }
}