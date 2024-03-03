package org.authenticationservice.constants;

@SuppressWarnings("java:S1214")
public interface ErrorMessage {
    String USERNAME_REQUIRED = "Username is required.";
    String USERNAME_LENGTH = "Username must be between 6 and 30 characters.";
    String USERNAME_EXISTS_ALREADY = "Username exists already";
    String PASSWORD_REQUIRED = "Password is required.";
    String PASSWORD_LENGTH = "Password must be between 8 and 72 characters.";
    String PASSWORD_CONSTRAINTS = "Password needs to contain at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/";
}
