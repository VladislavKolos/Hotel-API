package org.example.api.hotel.exception;

public abstract class HotelApiException extends RuntimeException {
    public HotelApiException(String message) {
        super(message);
    }
}