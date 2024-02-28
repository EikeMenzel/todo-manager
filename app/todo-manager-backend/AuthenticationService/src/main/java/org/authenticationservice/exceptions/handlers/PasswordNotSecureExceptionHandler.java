package org.authenticationservice.exceptions.handlers;

import org.authenticationservice.exceptions.PasswordNotSecureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class PasswordNotSecureExceptionHandler {
    @ExceptionHandler(PasswordNotSecureException.class)
    public ResponseEntity<Map<String, String>> handlePasswordNotSecureException(PasswordNotSecureException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("password", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
