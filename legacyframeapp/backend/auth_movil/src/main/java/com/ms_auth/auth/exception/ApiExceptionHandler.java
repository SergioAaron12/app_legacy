package com.ms_auth.auth.exception;

import com.ms_auth.auth.dto.ApiErrorDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ApiErrorDto> handleDuplicate(DuplicateFieldException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorDto(ex.getMessage(), ex.getField()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorDto(ex.getMessage(), "email"));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorDto(ex.getMessage(), "password"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDto> handleDataIntegrity(DataIntegrityViolationException ex) {
        // Fallback por si la BD rechaza un duplicado antes de que lo validemos.
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorDto("Ya existe un registro con esos datos", null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDto> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDto(ex.getMessage(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDto> handleRuntime(RuntimeException ex) {
        // Mantenerlo simple: devolvemos el mensaje para que la app lo muestre.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDto(ex.getMessage(), null));
    }
}
