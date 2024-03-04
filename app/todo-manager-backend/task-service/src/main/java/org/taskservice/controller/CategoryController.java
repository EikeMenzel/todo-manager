package org.taskservice.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.taskservice.clients.IDatabaseServiceClient;
import org.taskservice.exceptions.handler.ValidationExceptionHandler;
import org.taskservice.payload.CategoryDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final IDatabaseServiceClient databaseServiceClient;
    @PostMapping()
    public ResponseEntity<Void> saveCategory(@RequestHeader @NotNull @Positive Long userId, @RequestBody @Validated(ValidationExceptionHandler.OnCreate.class) CategoryDTO categoryDTO) {
        return databaseServiceClient.createCategory(userId, categoryDTO);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestHeader @NotNull @Positive Long userId) {
        return databaseServiceClient.getCategoryDTOList(userId);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@RequestHeader @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @RequestBody @Validated(ValidationExceptionHandler.OnUpdate.class) CategoryDTO categoryDTO) {
        return databaseServiceClient.updateCategory(userId, categoryId, categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@RequestHeader @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId) {
        return databaseServiceClient.deleteCategory(userId, categoryId);
    }
}
