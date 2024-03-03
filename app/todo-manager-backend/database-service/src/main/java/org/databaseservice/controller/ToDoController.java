package org.databaseservice.controller;

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
public class ToDoController {
    private final IToDoService toDoService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveToDo(@PathVariable @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @Validated(ValidationExceptionHandler.OnCreate.class) @RequestBody ToDoDTO toDoDTO) {
        toDoService.saveTodo(userId, categoryId, toDoDTO);
    }

    @GetMapping()
    public ResponseEntity<List<ToDoDTO>> getToDosFromCategory(@PathVariable @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId) {
        val toDoList = toDoService.getTodosFromCategory(userId, categoryId);
        return toDoList.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok(toDoList);
    }

    @PutMapping("/{toDoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateToDo(@PathVariable @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @PathVariable @NotNull @Positive Long toDoId, @RequestBody @Validated(ValidationExceptionHandler.OnUpdate.class) ToDoDTO toDoDTO) {
        toDoService.updateTodo(userId, categoryId, toDoId, toDoDTO);
    }

    @DeleteMapping("/{toDoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteToDo(@PathVariable @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @PathVariable @NotNull @Positive Long toDoId) {
        toDoService.deleteTodo(userId, categoryId, toDoId);
    }
}

