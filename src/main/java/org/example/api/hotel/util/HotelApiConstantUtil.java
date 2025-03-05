package org.example.api.hotel.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HotelApiConstantUtil {
    public static final String NAME = "name";
    public static final String CITY = "city";
    public static final String BRAND = "brand";
    public static final String ADDRESS = "address";
    public static final String COUNTRY = "country";
    public static final String BASE_PATH = "/property-view";
    public static final String HOTEL_PATH = BASE_PATH + "/hotels/{id}";
    public static final String HOTEL_AMENITY_PATH = BASE_PATH + "/hotels/{id}/amenities";
}
