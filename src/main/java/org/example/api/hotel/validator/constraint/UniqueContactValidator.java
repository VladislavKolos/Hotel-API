package org.example.api.hotel.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.repository.ContactRepository;
import org.example.api.hotel.validator.constraint.annotation.UniqueContact;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueContactValidator implements ConstraintValidator<UniqueContact, CreateHotelRequestDto> {
    private final ContactRepository contactRepository;

    @Override
    public boolean isValid(CreateHotelRequestDto request, ConstraintValidatorContext constraintValidatorContext) {
        String email = request.getContacts().getEmail();
        String phone = request.getContacts().getPhone();

        boolean emailExists = contactRepository.existsByEmail(email);
        boolean phoneExists = contactRepository.existsByPhone(phone);

        if (emailExists || phoneExists) {
            log.info("Hotel creation failed: email {} or phone {} already exists", email, phone);
            return false;
        }
        return true;
    }
}