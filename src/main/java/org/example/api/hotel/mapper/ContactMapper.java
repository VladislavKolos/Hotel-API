package org.example.api.hotel.mapper;

import org.example.api.hotel.dto.request.ContactRequestDto;
import org.example.api.hotel.dto.response.ContactResponseDto;
import org.example.api.hotel.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "id", ignore = true)
    Contact toEntity(ContactRequestDto dto);

    ContactResponseDto toContactResponseDto(Contact contact);
}