package com.checkout.data.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ApiExceptionResponse {

    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();

    HttpStatus httpStatus;

    String message;

    List<String> errors;
}
