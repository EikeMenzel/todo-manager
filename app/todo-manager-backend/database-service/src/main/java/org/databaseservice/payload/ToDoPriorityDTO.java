package org.databaseservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the priority levels for ToDo tasks.")
public enum ToDoPriorityDTO {
    @Schema(description = "Low priority indicating a task that is not urgent.")
    LOW,

    @Schema(description = "Medium priority for tasks that are of normal urgency.")
    MEDIUM,

    @Schema(description = "High priority indicating a task that requires immediate attention.")
    HIGH
}
