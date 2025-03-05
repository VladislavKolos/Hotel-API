package org.example.api.hotel.mapper;

import org.example.api.hotel.dto.request.ArrivalTimeRequestDto;
import org.example.api.hotel.dto.response.ArrivalTimeResponseDto;
import org.example.api.hotel.model.ArrivalTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Optional;

@Component
@Mapper(componentModel = "spring")
public interface ArrivalTimeMapper {

    @Mapping(target = "checkIn", source = "checkIn", qualifiedByName = "parseTime")
    @Mapping(target = "checkOut", source = "checkOut", qualifiedByName = "parseTime")
    ArrivalTime toEntity(ArrivalTimeRequestDto dto);

    @Mapping(target = "checkIn", source = "checkIn", qualifiedByName = "formatTime")
    @Mapping(target = "checkOut", source = "checkOut", qualifiedByName = "formatTime")
    ArrivalTimeResponseDto toArrivalTimeResponseDto(ArrivalTime arrivalTime);

    @Named("parseTime")
    default LocalTime parseTime(String time) {
        return Optional.ofNullable(time)
                .map(LocalTime::parse)
                .orElseThrow(() -> new IllegalArgumentException("Invalid time format"));
    }

    @Named("formatTime")
    default String formatTime(LocalTime time) {
        return Optional.ofNullable(time)
                .map(LocalTime::toString)
                .orElseThrow(() -> new IllegalArgumentException("ArrivalTime cannot be null"));
    }
}