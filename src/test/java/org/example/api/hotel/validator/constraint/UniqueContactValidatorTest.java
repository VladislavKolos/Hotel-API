package org.example.api.hotel.validator.constraint;

import jakarta.validation.ConstraintValidatorContext;
import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.repository.ContactRepository;
import org.example.api.hotel.util.TestDataBuilderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniqueContactValidatorTest {

    @InjectMocks
    private UniqueContactValidator uniqueContactValidator;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private CreateHotelRequestDto createHotelRequestDto;

    @BeforeEach
    public void setUp() {
        createHotelRequestDto = TestDataBuilderUtil.generateCreateHotelRequest();
    }

    @Test
    public void testIsValidWhenEmailAndPhoneAreUniqueReturnsTrue() {
        when(contactRepository.existsByEmail(createHotelRequestDto.getContacts().getEmail())).thenReturn(false);
        when(contactRepository.existsByPhone(createHotelRequestDto.getContacts().getPhone())).thenReturn(false);

        boolean isValid = uniqueContactValidator.isValid(createHotelRequestDto, constraintValidatorContext);

        assertTrue(isValid);

        verify(contactRepository).existsByEmail(createHotelRequestDto.getContacts().getEmail());
        verify(contactRepository).existsByPhone(createHotelRequestDto.getContacts().getPhone());
    }

    @Test
    public void testIsValidWhenEmailExistsReturnsFalse() {
        when(contactRepository.existsByEmail(createHotelRequestDto.getContacts().getEmail())).thenReturn(true);
        when(contactRepository.existsByPhone(createHotelRequestDto.getContacts().getPhone())).thenReturn(false);

        boolean isValid = uniqueContactValidator.isValid(createHotelRequestDto, constraintValidatorContext);

        assertFalse(isValid);

        verify(contactRepository).existsByEmail(createHotelRequestDto.getContacts().getEmail());
        verify(contactRepository).existsByPhone(createHotelRequestDto.getContacts().getPhone());
    }

    @Test
    public void testIsValidWhenPhoneExistsReturnsFalse() {
        when(contactRepository.existsByEmail(createHotelRequestDto.getContacts().getEmail())).thenReturn(false);
        when(contactRepository.existsByPhone(createHotelRequestDto.getContacts().getPhone())).thenReturn(true);

        boolean isValid = uniqueContactValidator.isValid(createHotelRequestDto, constraintValidatorContext);

        assertFalse(isValid);

        verify(contactRepository).existsByEmail(createHotelRequestDto.getContacts().getEmail());
        verify(contactRepository).existsByPhone(createHotelRequestDto.getContacts().getPhone());
    }

    @Test
    public void testIsValidWhenEmailAndPhoneExistReturnsFalse() {
        when(contactRepository.existsByEmail(createHotelRequestDto.getContacts().getEmail())).thenReturn(true);
        when(contactRepository.existsByPhone(createHotelRequestDto.getContacts().getPhone())).thenReturn(true);

        boolean isValid = uniqueContactValidator.isValid(createHotelRequestDto, constraintValidatorContext);

        assertFalse(isValid);

        verify(contactRepository).existsByEmail(createHotelRequestDto.getContacts().getEmail());
        verify(contactRepository).existsByPhone(createHotelRequestDto.getContacts().getPhone());
    }
}