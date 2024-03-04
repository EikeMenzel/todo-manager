package org.databaseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.databaseservice.exceptions.handler.ValidationExceptionHandler;
import org.databaseservice.payload.ToDoDTO;
import org.databaseservice.services.IToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/db/users/{userId}/categories/{categoryId}/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "ToDo Management", description = "API for managing ToDo tasks within user categories")
public class ToDoController {
    private final IToDoService toDoService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new ToDo task", description = "Creates a new ToDo task within a specific category for a user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ToDo task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or path variables"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void saveToDo(@PathVariable @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                         @PathVariable @NotNull @Positive @Parameter(description = "Category ID within which the ToDo is to be created", required = true) Long categoryId,
                         @Validated(ValidationExceptionHandler.OnCreate.class) @RequestBody @Parameter(description = "ToDo task details", required = true) ToDoDTO toDoDTO) {
        toDoService.saveTodo(userId, categoryId, toDoDTO);
    }

    @GetMapping()
    @Operation(summary = "Get ToDo tasks from a category", description = "Retrieves all ToDo tasks from a specific category for a user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ToDo tasks retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No ToDo tasks found"),
            @ApiResponse(responseCode = "400", description = "Invalid path variables")
    })
    public ResponseEntity<List<ToDoDTO>> getToDosFromCategory(@PathVariable @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                                                              @PathVariable @NotNull @Positive @Parameter(description = "Category ID from which ToDo tasks are to be retrieved", required = true) Long categoryId) {
        val toDoList = toDoService.getTodosFromCategory(userId, categoryId);
        return toDoList.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok(toDoList);
    }

    @PutMapping("/{toDoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a ToDo task", description = "Updates an existing ToDo task within a specific category for a user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ToDo task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or path variables"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void updateToDo(@PathVariable @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                           @PathVariable @NotNull @Positive @Parameter(description = "Category ID within which the ToDo exists", required = true) Long categoryId,
                           @PathVariable @NotNull @Positive @Parameter(description = "ToDo task ID to be updated", required = true) Long toDoId,
                           @RequestBody @Validated(ValidationExceptionHandler.OnUpdate.class) @Parameter(description = "Updated ToDo task details", required = true) ToDoDTO toDoDTO) {
        toDoService.updateTodo(userId, categoryId, toDoId, toDoDTO);
    }

    @DeleteMapping("/{toDoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a ToDo task", description = "Deletes an existing ToDo task within a specific category for a user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ToDo task deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid path variables"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteToDo(@PathVariable @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                           @PathVariable @NotNull @Positive @Parameter(description = "Category ID from which the ToDo task is to be deleted", required = true) Long categoryId,
                           @PathVariable @NotNull @Positive @Parameter(description = "ToDo task ID to be deleted", required = true) Long toDoId) {
        toDoService.deleteTodo(userId, categoryId, toDoId);
    }
}

