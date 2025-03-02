package org.example.api.hotel.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum HotelGroupingParameter {
    BRAND("brand"),
    CITY("city"),
    COUNTRY("country"),
    AMENITIES("amenities");

    private final String value;

    public static HotelGroupingParameter parseOrThrow(String param) {
        return Arrays.stream(values())
                .filter(e -> e.value.equalsIgnoreCase(param))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported grouping parameter: " + param)); //TODO Later I will create custom exceptions
    }
}