package org.example.api.hotel.util.uri.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.api.hotel.util.HotelApiConstantUtil;

@Getter
@RequiredArgsConstructor
public enum UriType {
    HOTEL(HotelApiConstantUtil.HOTEL_PATH),
    HOTEL_AMENITY(HotelApiConstantUtil.HOTEL_AMENITY_PATH);

    private final String path;
}