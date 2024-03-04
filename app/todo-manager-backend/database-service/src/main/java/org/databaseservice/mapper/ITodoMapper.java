package org.databaseservice.mapper;

import org.databaseservice.models.TodoPriority;
import org.databaseservice.models.TodoStatus;
import org.databaseservice.payload.ToDoPriorityDTO;
import org.databaseservice.payload.ToDoStatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ITodoMapper {
    @Named("mapPriority")
    default TodoPriority mapPriority(ToDoPriorityDTO priorityDTO) {
        if (priorityDTO == null)
            return null;

        return TodoPriority.valueOf(priorityDTO.name());
    }

    @Named("mapStatus")
    default TodoStatus mapStatus(ToDoStatusDTO statusDTO) {
        if (statusDTO == null)
            return null;

        return TodoStatus.valueOf(statusDTO.name());
    }

    @Named("mapPriorityToDto")
    default ToDoPriorityDTO mapPriorityToDto(TodoPriority priority) {
        if (priority == null)
            return null;

        return ToDoPriorityDTO.valueOf(priority.name());
    }

    @Named("mapStatusToDto")
    default ToDoStatusDTO mapStatusToDto(TodoStatus status) {
        if (status == null)
            return null;

        return ToDoStatusDTO.valueOf(status.name());
    }
}

