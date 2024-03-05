package com.checkout.data.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class ApiExceptionResponse {

  @Builder.Default LocalDateTime timestamp = LocalDateTime.now();

  HttpStatus httpStatus;

  String message;

  List<String> errors;
}
