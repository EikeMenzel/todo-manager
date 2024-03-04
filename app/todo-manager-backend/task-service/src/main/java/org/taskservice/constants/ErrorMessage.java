package org.taskservice.constants;

public abstract class ErrorMessage {
    public static final String CATEGORY_REQUIRED = "Category name is required.";
    public static final String CATEGORY_LENGTH = "Category name must be between 1 and 32 characters.";
    public static final String ID_MUST_BE_POSITIVE = "Id must be positive.";
    public static final String TEXT_REQUIRED = "The text field must not be blank.";
    public static final String PRIORITY_REQUIRED = "The priority field must not be null and must be one of the defined states.";
    public static final String STATUS_REQUIRED = "The status field must not be null and must be one of the defined states.";
    public static final String TODO_ID_REQUIRED = "TodoId is required.";
    public static final String ID_CREATION = "ID must be null when creating";
    public static final String CATEGORY_ID_REQUIRED = "CategoryId is required.";

    private ErrorMessage() {}
}
