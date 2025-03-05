package org.example.api.hotel.exception;

public class InvalidGroupingParameterException extends HotelApiException {
    public InvalidGroupingParameterException(String parameter) {
        super("Unsupported grouping parameter: " + parameter);
    }
}