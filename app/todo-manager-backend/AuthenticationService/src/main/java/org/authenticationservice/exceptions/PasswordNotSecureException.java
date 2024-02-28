package org.authenticationservice.exceptions;

public class PasswordNotSecureException extends RuntimeException {
    public PasswordNotSecureException(String message) {
        super(message);
    }
}
