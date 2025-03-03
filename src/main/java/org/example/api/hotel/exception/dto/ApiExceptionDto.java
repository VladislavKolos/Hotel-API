package org.example.api.hotel.exception.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiExceptionDto(Integer status,
                              String message,
                              String error,
                              String path,
                              LocalDateTime timestamp) {
}