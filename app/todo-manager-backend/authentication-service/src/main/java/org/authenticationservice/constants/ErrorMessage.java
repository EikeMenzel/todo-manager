package org.authenticationservice.constants;

public abstract class ErrorMessage {
    public static final String USERNAME_REQUIRED = "Username is required.";
    public static final String USERNAME_LENGTH = "Username must be between 6 and 30 characters.";
    public static final String USERNAME_EXISTS_ALREADY = "Username exists already";
    public static final String PASSWORD_REQUIRED = "Password is required.";
    public static final String PASSWORD_LENGTH = "Password must be between 8 and 72 characters.";
    public static final String PASSWORD_CONSTRAINTS = "Password needs to contain at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/";

    private ErrorMessage() {}
}
