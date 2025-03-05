package org.example.api.hotel.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiExceptionDto(Integer status,
                              String message,
                              String error,
                              String path,
                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime timestamp) {
}