package org.example.api.hotel.mapper;

import org.example.api.hotel.dto.request.AddressRequestDto;
import org.example.api.hotel.dto.response.AddressResponseDto;
import org.example.api.hotel.model.Address;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressRequestDto dto);

    AddressResponseDto toAddressResponseDto(Address address);
}
