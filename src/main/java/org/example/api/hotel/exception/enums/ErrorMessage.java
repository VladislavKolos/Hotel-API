package org.example.api.hotel.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    ENTITY_HOTEL_NOT_FOUND(
            "Hotel with the given ID was not found.",
            HttpStatus.NOT_FOUND),
    AMENITY_ALREADY_EXISTS(
            "All provided amenities already exist for this hotel.",
            HttpStatus.BAD_REQUEST),
    ENTITY_HOTEL_SAVE_ERROR(
            "Failed to save the entity. Please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_GROUPING_PARAMETER(
            "Unsupported grouping parameter provided.",
            HttpStatus.BAD_REQUEST),
    HOTEL_API_EXCEPTION(
            """
                    An application error occurred in the hotel API. Please try again later or contact support:
                    support@hotel-api.com""",
            HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_ARGUMENT_NOT_VALID_ERROR(
            "Validation failed. Ensure all fields are correctly filled.",
            HttpStatus.BAD_REQUEST),
    RUNTIME_ERROR(
            "A runtime error occurred. Please try again later or contact support: support@hotel-api.com",
            HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR(
            "An unexpected error occurred. Please try again later or contact support: support@hotel-api.com",
            HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_ARGUMENT_TYPE_MISMATCH_ERROR(
            "Invalid parameter type. Please check the request and try again.",
            HttpStatus.BAD_REQUEST),
    CONSTRAINT_VIOLATION_ERROR(
            "Validation failed. Ensure all fields are correctly filled.",
            HttpStatus.BAD_REQUEST),
    ILLEGAL_STATE_ERROR(
            "Operation cannot be completed due to the current system state. Please check the data or try again later.",
            HttpStatus.FORBIDDEN),
    NO_SUCH_ELEMENT_ERROR(
            "The requested resource was not found.",
            HttpStatus.NOT_FOUND),
    NULL_POINTER_ERROR(
            "An internal error occurred. Please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR),
    ILLEGAL_ARGUMENT_ERROR(
            "Invalid input provided. Please verify the data and try again.",
            HttpStatus.BAD_REQUEST),
    EXPIRED_JWT_ERROR(
            "Your session has expired. Please log in again to continue.",
            HttpStatus.UNAUTHORIZED),
    NO_RESOURCE_FOUND_ERROR(
            "The requested resource could not be found. Please check your request and try again.",
            HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}