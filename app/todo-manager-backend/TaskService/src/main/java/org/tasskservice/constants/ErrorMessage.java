package org.tasskservice.constants;

public interface ErrorMessage {
    String CATEGORY_REQUIRED = "Category name is required.";
    String CATEGORY_LENGTH = "Category name must be between 1 and 32 characters.";
    String ID_MUST_BE_POSITIVE = "Id must be positive.";
    String TEXT_REQUIRED = "The text field must not be blank.";
    String PRIORITY_REQUIRED = "The priority field must not be null and must be one of the defined states.";
    String STATUS_REQUIRED = "The status field must not be null and must be one of the defined states.";
    String TODO_NOT_FOUND = "ToDo not found.";
    String TODO_ID_REQUIRED = "TodoId is required.";
}
