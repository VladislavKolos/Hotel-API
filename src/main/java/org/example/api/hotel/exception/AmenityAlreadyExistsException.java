package org.example.api.hotel.exception;

import java.util.List;

public class AmenityAlreadyExistsException extends HotelApiException {
    public AmenityAlreadyExistsException(String hotelName, List<String> amenities) {
        super("The following amenities already exist for hotel '" + hotelName + "': " + String.join(", ", amenities));
    }
}