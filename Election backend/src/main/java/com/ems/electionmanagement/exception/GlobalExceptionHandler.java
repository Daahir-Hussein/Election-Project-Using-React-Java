package com.ems.electionmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
        ResourceNotFoundException ex,
        HttpServletRequest request
    ) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
        BadRequestException ex,
        HttpServletRequest request
    ) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(
        ConflictException ex,
        HttpServletRequest request
    ) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        ex.getBindingResult().getGlobalErrors().forEach(error ->
            errors.putIfAbsent(error.getObjectName(), error.getDefaultMessage())
        );

        String message = errors.values().stream()
            .filter(value -> value != null && !value.isBlank())
            .distinct()
            .collect(Collectors.joining(" "));

        if (message.isBlank()) {
            message = "Validation failed.";
        }

        return build(HttpStatus.BAD_REQUEST, message, request, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation ->
            errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        String message = String.join(" ", errors.values());
        return build(HttpStatus.BAD_REQUEST, message, request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableBody(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.BAD_REQUEST,
            "The request body is invalid. Check date formats and field values.",
            request,
            Map.of()
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUpload(
        MaxUploadSizeExceededException ex,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.PAYLOAD_TOO_LARGE,
            "The selected file is too large. Maximum file size is 5 MB.",
            request,
            Map.of()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.CONFLICT,
            "The operation conflicts with existing data or a unique field already exists.",
            request,
            Map.of()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthentication(
        AuthenticationException ex,
        HttpServletRequest request
    ) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid username or password.", request, Map.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
        AccessDeniedException ex,
        HttpServletRequest request
    ) {
        return build(HttpStatus.FORBIDDEN, "You do not have permission to perform this action.", request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(
        Exception ex,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected server error occurred.",
            request,
            Map.of()
        );
    }

    private ResponseEntity<ApiErrorResponse> build(
        HttpStatus status,
        String message,
        HttpServletRequest request,
        Map<String, String> errors
    ) {
        ApiErrorResponse body = new ApiErrorResponse(
            OffsetDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI(),
            errors
        );
        return ResponseEntity.status(status).body(body);
    }
}
