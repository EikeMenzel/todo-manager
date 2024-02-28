package org.authenticationservice.exceptions.handlers;

import org.authenticationservice.exceptions.UsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UsernameExistsExceptionHandler {
    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameExistsException(UsernameExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("username", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }
}
