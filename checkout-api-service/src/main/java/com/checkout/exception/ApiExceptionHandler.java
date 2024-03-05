package com.checkout.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.checkout.data.dto.ApiExceptionResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiExceptionResponse> handleEntityNotFoundException(
      EntityNotFoundException ex) {
    return ResponseEntity.status(NOT_FOUND)
        .body(
            ApiExceptionResponse.builder()
                .message(ex.getLocalizedMessage())
                .httpStatus(NOT_FOUND)
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiExceptionResponse> handleGenericException(Exception ex) {
    log.error("Internal server error", ex);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(
            ApiExceptionResponse.builder()
                .message(ex.getLocalizedMessage())
                .httpStatus(INTERNAL_SERVER_ERROR)
                .build());
  }

  @Override
  protected ResponseEntity<Object> handleHandlerMethodValidationException(
      HandlerMethodValidationException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    var errorMessages = buildErrorList(ex.getAllValidationResults());
    return ResponseEntity.status(status)
        .body(
            ApiExceptionResponse.builder()
                .message(ex.getReason())
                .httpStatus(HttpStatus.valueOf(status.value()))
                .errors(errorMessages)
                .build());
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {
    return ResponseEntity.status(statusCode)
        .body(
            ApiExceptionResponse.builder()
                .message(ex.getLocalizedMessage())
                .httpStatus(HttpStatus.valueOf(statusCode.value()))
                .build());
  }

  private List<String> buildErrorList(List<ParameterValidationResult> allValidationResults) {
    return allValidationResults.stream()
        .flatMap(vr -> vr.getResolvableErrors().stream().map(re -> re.getDefaultMessage()))
        .toList();
  }
}
