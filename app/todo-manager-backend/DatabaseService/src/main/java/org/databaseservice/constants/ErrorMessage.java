package org.databaseservice.constants;

public interface ErrorMessage {
    String USER_NOT_FOUND = "User not found";
    String USERNAME_REQUIRED = "Username is required.";
    String USERNAME_LENGTH = "Username must be between 6 and 30 characters.";
    String PASSWORD_REQUIRED = "Password is required.";
    String PASSWORD_LENGTH = "Password must be between 8 and 72 characters.";

    String CATEGORY_REQUIRED = "Category name is required.";
    String CATEGORY_LENGTH = "Category name must be between 1 and 32 characters.";
    String CATEGORY_MOT_FOUND = "Category not found.";
    String UPDATE_ERROR = "Error occurred during the update.";
    String SAVE_ERROR = "Error occurred during the save.";
    String BAD_REQUEST_IDS_MISMATCH = "Ids are not the same.";
    String PERMISSION_DENIED = "You are not allowed to modify this resource";
}
