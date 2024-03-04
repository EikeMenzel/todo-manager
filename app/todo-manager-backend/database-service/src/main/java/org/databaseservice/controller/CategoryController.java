package org.databaseservice.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.databaseservice.exceptions.handler.ValidationExceptionHandler;
import org.databaseservice.payload.CategoryDTO;
import org.databaseservice.services.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/db/users/{userId}/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@PathVariable @NotNull @Positive Long userId, @RequestBody @Validated(ValidationExceptionHandler.OnCreate.class) CategoryDTO categoryDTO) {
        categoryService.saveCategory(userId, categoryDTO);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getCategories(@PathVariable @NotNull @Positive Long userId) {
        val result = categoryService.getCategories(userId);
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId, @RequestBody @Validated(ValidationExceptionHandler.OnUpdate.class) CategoryDTO categoryDTO) {
        categoryService.updateCategory(userId, categoryId, categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull @Positive Long userId, @PathVariable @NotNull @Positive Long categoryId) {
        categoryService.deleteCategory(userId, categoryId);
    }
}
