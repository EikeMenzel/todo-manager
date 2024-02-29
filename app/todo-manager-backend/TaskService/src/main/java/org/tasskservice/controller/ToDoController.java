package org.tasskservice.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tasskservice.clients.IDatabaseServiceClient;
import org.tasskservice.exceptions.handler.ValidationExceptionHandler;
import org.tasskservice.payload.ToDoDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories/{categoryId}/tasks")
@RequiredArgsConstructor
public class ToDoController {
    private final IDatabaseServiceClient databaseServiceClient;
    @PostMapping()
    public ResponseEntity<Void> saveToDo(@RequestHeader @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @RequestBody @Validated(ValidationExceptionHandler.OnCreate.class) ToDoDTO toDoDTO) {
        return databaseServiceClient.createToDo(userId, categoryId, toDoDTO);
    }

    @GetMapping()
    public ResponseEntity<List<ToDoDTO>> getTodos(@RequestHeader @NotNull @Positive Long userId, @PathVariable @NotNull @Positive String categoryId) {
        return databaseServiceClient.getToDoDTOList(userId, categoryId);
    }

    @PutMapping("/{toDoId}")
    public ResponseEntity<Void> updateTodo(@RequestHeader @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @PathVariable @NotNull @Positive Long toDoId, @RequestBody @Validated(ValidationExceptionHandler.OnUpdate.class) ToDoDTO toDoDTO) {
        return databaseServiceClient.updateToDo(userId, categoryId, toDoId, toDoDTO);
    }

    @DeleteMapping("/{toDoId}")
    public ResponseEntity<Void> deleteToDo(@RequestHeader @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @PathVariable @NotNull @Positive Long toDoId) {
        return databaseServiceClient.deleteToDo(userId, categoryId, toDoId);
    }
}
