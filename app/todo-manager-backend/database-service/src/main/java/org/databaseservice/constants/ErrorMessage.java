package org.databaseservice.constants;

public abstract class ErrorMessage {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USERNAME_REQUIRED = "Username is required.";
    public static final String USERNAME_LENGTH = "Username must be between 6 and 30 characters.";
    public static final String PASSWORD_REQUIRED = "Password is required.";
    public static final String PASSWORD_LENGTH = "Password must be between 8 and 72 characters.";
    public static final String CATEGORY_REQUIRED = "Category name is required.";
    public static final String CATEGORY_LENGTH = "Category name must be between 1 and 32 characters.";
    public static final String CATEGORY_MOT_FOUND = "Category not found.";
    public static final String CATEGORY_ID_REQUIRED = "TodoId is required.";
    public static final String UPDATE_ERROR = "Error occurred during the update.";
    public static final String SAVE_ERROR = "Error occurred during the save.";
    public static final String DELETE_ERROR = "Error occurred during the deletion";
    public static final String BAD_REQUEST_IDS_MISMATCH = "Ids are not the same.";
    public static final String PERMISSION_DENIED = "You are not allowed to interact with this resource.";
    public static final String ID_MUST_BE_POSITIVE = "Id must be positive.";
    public static final String TEXT_REQUIRED = "The text field must not be blank.";
    public static final String PRIORITY_REQUIRED = "The priority field must not be null and must be one of the defined states.";
    public static final String STATUS_REQUIRED = "The status field must not be null and must be one of the defined states.";
    public static final String TODO_NOT_FOUND = "ToDo not found.";
    public static final String TODO_ID_REQUIRED = "TodoId is required.";
    public static final String ID_CREATION = "ID must be null when creating";

    private ErrorMessage() {}
}
