package com.example.userservice.controller;

import com.example.userservice.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private Map<String, Object> buildBaseErrorResponse(HttpStatus status, String message, String path, String requestId) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", Instant.now().toString());
        response.put("path", path);
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        response.put("requestId", requestId);
        return response;
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(
            HttpStatus status,
            String message,
            String path,
            String requestId
    ) {
        Map<String, Object> errorResponse = buildBaseErrorResponse(status, message, path, requestId);
        log.error("Ошибка: {} - {}", status, message);
        return ResponseEntity.status(status).body(errorResponse);
    }


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex, HttpServletRequest request) {
        return createErrorResponse(
                ex.getStatus(),
                ex.getMessage(),
                request.getRequestURI(),
                request.getRequestId()
        );
    }

     @ExceptionHandler(HttpMessageNotReadableException.class)
     public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                request.getRequestId()
        );
     }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request
    ){
        return createErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getMessage(),
                request.getRequestURI(),
                request.getRequestId()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                request.getRequestId()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request.getRequestURI(), request.getRequestId());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        Map<String, Object> errorResponse = buildBaseErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), request.getRequestId());
        errorResponse.put("details", violations);
        log.error("Constraint violations: {}", violations, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        Map<String, Object> errorResponse = buildBaseErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Parameter validation error",
                request.getRequestURI(),
                request.getRequestId()
        );
        errorResponse.put("details", validationErrors);
        log.error("Validation Error: {}", validationErrors, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An internal server error occurred. Please try again later.",
                request.getRequestURI(),
                request.getRequestId()
        );
    }
}
