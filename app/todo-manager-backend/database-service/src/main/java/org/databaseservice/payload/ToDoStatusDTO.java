package org.databaseservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents the current status of a ToDo task.")
public enum ToDoStatusDTO {
    @Schema(description = "Indicates that the task has not been started yet.")
    NOT_STARTED,

    @Schema(description = "The task is currently in progress.")
    IN_PROGRESS,

    @Schema(description = "The task has been completed.")
    FINISHED
}
