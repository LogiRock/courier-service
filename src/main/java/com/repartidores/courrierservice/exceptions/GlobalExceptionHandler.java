package com.repartidores.courrierservice.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.repartidores.courrierservice.dto.ErrorResponseDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import feign.FeignException;

/** Convierte errores de la API a la estructura común de ErrorResponseDto. */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(
            EntityNotFoundException ex, WebRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return buildError(HttpStatus.BAD_REQUEST,
                "Los datos enviados no son válidos.", request, validationErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidParameter(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = "El parámetro '" + ex.getName() + "' tiene un formato o valor inválido.";
        return buildError(HttpStatus.BAD_REQUEST, message, request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleUnreadableBody(
            HttpMessageNotReadableException ex, WebRequest request) {
        return buildError(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición contiene datos inválidos o mal formados.", request, null);
    }
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponseDto> handleFeign(
            FeignException ex, WebRequest request) {
        log.warn("security-service respondió {} al registrar el usuario", ex.status());
        return switch (ex.status()) {
            case 409 -> buildError(HttpStatus.CONFLICT,
                    "El nombre de usuario ya existe.", request, null);
            case 400 -> buildError(HttpStatus.BAD_REQUEST,
                    "Los datos del usuario no son válidos.", request, null);
            default -> buildError(HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo crear el usuario. Intentá nuevamente.", request, null);
        };
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpected(
            Exception ex, WebRequest request) {
        log.error("Error inesperado atendiendo {}", request.getDescription(false), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor.", request, null);
    }

    private ResponseEntity<ErrorResponseDto> buildError(
            HttpStatus status, String message, WebRequest request, List<String> validationErrors) {
        ErrorResponseDto body = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .validationErrors(validationErrors)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
