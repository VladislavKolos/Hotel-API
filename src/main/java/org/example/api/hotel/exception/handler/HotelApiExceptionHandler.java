package org.example.api.hotel.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.hotel.exception.AmenityAlreadyExistsException;
import org.example.api.hotel.exception.EntityHotelNotFoundException;
import org.example.api.hotel.exception.EntityHotelSaveException;
import org.example.api.hotel.exception.HotelApiException;
import org.example.api.hotel.exception.InvalidGroupingParameterException;
import org.example.api.hotel.exception.builder.ValidationExceptionMessageBuilder;
import org.example.api.hotel.exception.dto.ApiExceptionDto;
import org.example.api.hotel.exception.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class HotelApiExceptionHandler {
    private final ValidationExceptionMessageBuilder validationExceptionMessageBuilder;

    @ExceptionHandler(HotelApiException.class)
    public ResponseEntity<ApiExceptionDto> handleHotelApiException(HotelApiException ex, WebRequest request) {
        log.warn("Hotel API exception occurred at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.HOTEL_API_EXCEPTION, getRequestPath(request));
    }

    @ExceptionHandler(EntityHotelNotFoundException.class)
    public ResponseEntity<ApiExceptionDto> handleEntityHotelNotFoundException(EntityHotelNotFoundException ex,
                                                                              WebRequest request) {
        log.warn("Hotel not found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.ENTITY_HOTEL_NOT_FOUND, getRequestPath(request));
    }

    @ExceptionHandler(AmenityAlreadyExistsException.class)
    public ResponseEntity<ApiExceptionDto> handleAmenityAlreadyExistsException(AmenityAlreadyExistsException ex,
                                                                               WebRequest request) {
        log.warn("Attempt to add existing amenities at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.AMENITY_ALREADY_EXISTS, getRequestPath(request));
    }

    @ExceptionHandler(EntityHotelSaveException.class)
    public ResponseEntity<ApiExceptionDto> handleEntityHotelSaveException(EntityHotelSaveException ex,
                                                                          WebRequest request) {
        log.error("Failed to save entity at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.ENTITY_HOTEL_SAVE_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(InvalidGroupingParameterException.class)
    public ResponseEntity<ApiExceptionDto> handleInvalidGroupingParameterException(InvalidGroupingParameterException ex,
                                                                                   WebRequest request) {
        log.warn("Invalid grouping parameter at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.INVALID_GROUPING_PARAMETER, getRequestPath(request));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiExceptionDto> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.warn("Invalid parameter type at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.METHOD_ARGUMENT_TYPE_MISMATCH_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDto> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                  WebRequest request) {
        var result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            String errorMessage = validationExceptionMessageBuilder.buildValidationErrorMessage(fieldErrors);
            log.warn("Validation error at [{}]: {}", getRequestPath(request), errorMessage);

            return buildExceptionResponse(ErrorMessage.METHOD_ARGUMENT_NOT_VALID_ERROR.getStatus(),
                    getRequestPath(request), errorMessage);
        }
        log.warn("Constraint violation occurred at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.METHOD_ARGUMENT_NOT_VALID_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiExceptionDto> handleConstraintViolationException(ConstraintViolationException ex,
                                                                              WebRequest request) {
        log.warn("Constraint violation occurred at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.CONSTRAINT_VIOLATION_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiExceptionDto> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        log.error("Illegal state exception at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.ILLEGAL_STATE_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiExceptionDto> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        log.warn("No such element found at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.NO_SUCH_ELEMENT_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiExceptionDto> handleNullPointerException(NullPointerException ex, WebRequest request) {
        log.error("Null pointer exception at [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        return buildExceptionResponse(ErrorMessage.NULL_POINTER_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiExceptionDto> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                          WebRequest request) {

        log.warn("Illegal argument exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.ILLEGAL_ARGUMENT_ERROR, getRequestPath(request));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiExceptionDto> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                          WebRequest request) {

        log.warn("No resource found exception at [{}]: {}", getRequestPath(request), ex.getMessage());

        return buildExceptionResponse(ErrorMessage.NO_RESOURCE_FOUND_ERROR, getRequestPath(request));
    }


    private String getRequestPath(WebRequest request) {
        return request.getDescription(false)
                .replace("uri=", "");
    }

    private ResponseEntity<ApiExceptionDto> buildExceptionResponse(ErrorMessage errorMessage, String path) {
        return buildExceptionResponse(errorMessage.getStatus(), path, errorMessage.getMessage());
    }

    private ResponseEntity<ApiExceptionDto> buildExceptionResponse(HttpStatus status, String path,
                                                                   String customMessage) {
        log.error("Error occurred: {} at path: {}", customMessage, path);

        var apiException = ApiExceptionDto.builder()
                .status(status.value())
                .message(customMessage)
                .error(status.getReasonPhrase())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiException, status);
    }
}