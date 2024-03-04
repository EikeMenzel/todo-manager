package org.databaseservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.handler.ValidationExceptionHandler;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "ToDo Data Transfer Object")
public class ToDoDTO {
    @Null(groups = ValidationExceptionHandler.OnCreate.class, message = ErrorMessage.ID_CREATION)
    @NotNull(message = ErrorMessage.TODO_ID_REQUIRED, groups = ValidationExceptionHandler.OnUpdate.class)
    @Positive(message = ErrorMessage.ID_MUST_BE_POSITIVE)
    @Schema(description = "Unique identifier for the ToDo task. Required for update operations; must be null for creation.", example = "1", accessMode = Schema.AccessMode.READ_WRITE)
    private Long id;

    @NotBlank(message = ErrorMessage.TEXT_REQUIRED)
    @Schema(description = "Text description of the ToDo task.", example = "Finish the monthly report.")
    private String text;

    @NotNull(message = ErrorMessage.STATUS_REQUIRED)
    @Schema(description = "Status of the ToDo task indicating its current state.")
    private ToDoStatusDTO status;

    @NotNull(message = ErrorMessage.PRIORITY_REQUIRED)
    @Schema(description = "Priority of the ToDo task, indicating its urgency.")
    private ToDoPriorityDTO priority;

    @Positive(message = ErrorMessage.ID_MUST_BE_POSITIVE)
    @Schema(description = "Identifier of the category to which the ToDo task belongs.", example = "3")
    private Long categoryId;
}
