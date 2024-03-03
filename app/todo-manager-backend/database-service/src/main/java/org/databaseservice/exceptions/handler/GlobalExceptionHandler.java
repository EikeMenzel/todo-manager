package org.databaseservice.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.databaseservice.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorBody(ex, request), ex.getStatusCode());
    }

    @ExceptionHandler(SaveException.class)
    public ResponseEntity<Map<String, Object>> handleSaveException(SaveException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorBody(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<Map<String, Object>> handleUpdateException(UpdateException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorBody(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<Map<String, Object>> handleDeleteException(DeleteException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorBody(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorBody(ex, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorBody(ex, request), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> buildErrorBody(Exception exception, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());

        if (exception instanceof ResponseStatusException responseStatusException) {
            body.put("status", responseStatusException.getStatusCode().value());
            body.put("error", responseStatusException.getReason());
        } else {
            body.put("error", exception.getMessage());
        }

        body.put("path", webRequest.getDescription(false).substring(4));
        return body;
    }
}
