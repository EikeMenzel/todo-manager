package org.databaseservice.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.handler.ValidationExceptionHandler;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ToDoDTO {
    @NotNull(message = ErrorMessage.TODO_ID_REQUIRED, groups = ValidationExceptionHandler.OnUpdate.class)
    @Positive(message = ErrorMessage.ID_MUST_BE_POSITIVE)
    private Long id;

    @NotBlank(message = ErrorMessage.TEXT_REQUIRED)
    private String text;

    @NotNull(message = ErrorMessage.STATUS_REQUIRED)
    private ToDoStatusDTO status;

    @NotNull(message = ErrorMessage.PRIORITY_REQUIRED)
    private ToDoPriorityDTO priority;


    @Positive(message = ErrorMessage.ID_MUST_BE_POSITIVE)
    private Long categoryId;
}
