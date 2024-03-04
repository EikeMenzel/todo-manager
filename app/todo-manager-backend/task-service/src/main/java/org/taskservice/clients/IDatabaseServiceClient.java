package org.taskservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taskservice.payload.CategoryDTO;
import org.taskservice.payload.ToDoDTO;

import java.util.List;

@FeignClient(name = "database-service")
public interface IDatabaseServiceClient {
    @Value("${database.base.api.path}")
    String BASE_PATH = "";
    String CATEGORIES_PATH = BASE_PATH  + "/users/{userId}/categories";
    String TASK_PATH = CATEGORIES_PATH + "/{categoryId}/tasks";

    @PostMapping(CATEGORIES_PATH)
    ResponseEntity<Void> createCategory(@PathVariable Long userId, @RequestBody CategoryDTO categoryDTO);

    @PutMapping(CATEGORIES_PATH + "/{categoryId}")
    ResponseEntity<Void> updateCategory(@PathVariable Long userId, @PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO);

    @GetMapping(CATEGORIES_PATH)
    ResponseEntity<List<CategoryDTO>> getCategoryDTOList(@PathVariable Long userId);

    @DeleteMapping(CATEGORIES_PATH + "/{categoryId}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long userId, @PathVariable Long categoryId);

    @PostMapping(TASK_PATH)
    ResponseEntity<Void> createToDo(@PathVariable Long userId, @PathVariable Long categoryId, @RequestBody ToDoDTO toDoDTO);

    @PutMapping(TASK_PATH + "/{toDoId}")
    ResponseEntity<Void> updateToDo(@PathVariable Long userId, @PathVariable Long categoryId, @PathVariable Long toDoId, @RequestBody ToDoDTO toDoDTO);

    @GetMapping(TASK_PATH)
    ResponseEntity<List<ToDoDTO>> getToDoDTOList(@PathVariable Long userId, @PathVariable String categoryId);

    @DeleteMapping(TASK_PATH + "/{toDoId}")
    ResponseEntity<Void> deleteToDo(@PathVariable Long userId, @PathVariable Long categoryId, @PathVariable Long toDoId);
}
