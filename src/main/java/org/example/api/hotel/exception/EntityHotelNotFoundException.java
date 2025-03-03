package org.example.api.hotel.exception;

public class EntityHotelNotFoundException extends HotelApiException {
    public EntityHotelNotFoundException(String message) {
        super(message);
    }
}