package org.example.api.hotel.mapper;

import org.example.api.hotel.dto.request.CreateHotelRequestDto;
import org.example.api.hotel.dto.response.FullHotelInfoResponseDto;
import org.example.api.hotel.dto.response.ShortHotelInfoResponseDto;
import org.example.api.hotel.model.Address;
import org.example.api.hotel.model.Hotel;
import org.example.api.hotel.model.HotelAmenity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = {AddressMapper.class,
        ContactMapper.class,
        ArrivalTimeMapper.class})
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "contacts", target = "contact")
    Hotel toEntity(CreateHotelRequestDto dto);

    @Mapping(source = "hotelAmenities", target = "amenities", qualifiedByName = "mapAmenities")
    @Mapping(source = "contact", target = "contacts")
    FullHotelInfoResponseDto toFullHotelInfoResponseDto(Hotel hotel);

    @Mapping(target = "address", expression = "java(mapAddress(hotel.getAddress()))")
    @Mapping(target = "phone", source = "contact.phone")
    ShortHotelInfoResponseDto toShortHotelInfoResponseDto(Hotel hotel);

    @Named("mapAmenities")
    default List<String> mapAmenities(List<HotelAmenity> hotelAmenities) {
        if (hotelAmenities == null) {
            return List.of();
        }
        return hotelAmenities.stream()
                .map(ha -> ha.getAmenity().getName())
                .toList();
    }

    default String mapAddress(Address address) {
        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry());
    }
}