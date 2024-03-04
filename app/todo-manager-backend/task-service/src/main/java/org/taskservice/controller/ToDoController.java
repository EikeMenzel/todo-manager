package org.taskservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.taskservice.clients.IDatabaseServiceClient;
import org.taskservice.exceptions.handler.ValidationExceptionHandler;
import org.taskservice.payload.ToDoDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories/{categoryId}/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "ToDo Management", description = "API for managing ToDo tasks within categories")
public class ToDoController {
    private final IDatabaseServiceClient databaseServiceClient;
    @PostMapping()
    @Operation(summary = "Create a new ToDo task", description = "Creates a new ToDo task within a specific category for the user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ToDo task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or path variables"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> saveToDo(@RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                                         @PathVariable @NotNull @Positive @Parameter(description = "Category ID within which the ToDo task is to be created", required = true) Long categoryId,
                                         @RequestBody @Validated(ValidationExceptionHandler.OnCreate.class) @Parameter(description = "ToDo task details", required = true) ToDoDTO toDoDTO) {
        return databaseServiceClient.createToDo(userId, categoryId, toDoDTO);
    }

    @GetMapping()
    @Operation(summary = "Get ToDo tasks", description = "Retrieves all ToDo tasks within a specific category for the user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ToDo tasks retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No categories exist"),
            @ApiResponse(responseCode = "400", description = "Invalid path variable"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<List<ToDoDTO>> getTodos(@RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                                                  @PathVariable @NotNull @Positive @Parameter(description = "Category ID from which ToDo tasks are to be retrieved", required = true) Long categoryId) {
        return databaseServiceClient.getToDoDTOList(userId, categoryId);
    }

    @PutMapping("/{toDoId}")
    @Operation(summary = "Update a ToDo task", description = "Updates an existing ToDo task within a specific category for the user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ToDo task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or path variables"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "ToDo task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> updateTodo(@RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                                           @PathVariable @NotNull @Positive @Parameter(description = "Category ID within which the ToDo task exists", required = true) Long categoryId,
                                           @PathVariable @NotNull @Positive @Parameter(description = "ToDo task ID to be updated", required = true) Long toDoId,
                                           @RequestBody @Validated(ValidationExceptionHandler.OnUpdate.class) @Parameter(description = "Updated ToDo task details", required = true) ToDoDTO toDoDTO) {
        return databaseServiceClient.updateToDo(userId, categoryId, toDoId, toDoDTO);
    }

    @DeleteMapping("/{toDoId}")
    @Operation(summary = "Delete a ToDo task", description = "Deletes an existing ToDo task within a specific category for the user", tags = { "ToDo Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ToDo task deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid path variables"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "ToDo task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteToDo(@RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
                                           @PathVariable @NotNull @Positive @Parameter(description = "Category ID from which the ToDo task is to be deleted", required = true) Long categoryId,
                                           @PathVariable @NotNull @Positive @Parameter(description = "ToDo task ID to be deleted", required = true) Long toDoId) {
        return databaseServiceClient.deleteToDo(userId, categoryId, toDoId);
    }
}
