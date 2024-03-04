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
@Tag(name = "Category Management", description = "API for managing user categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category", description = "Creates a new category for the specified user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or path variable"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void createCategory(@PathVariable @NotNull @Positive @Parameter(description = "User ID for whom the category is being created", required = true) Long userId,
                               @RequestBody @Validated(ValidationExceptionHandler.OnCreate.class) @Parameter(description = "Category details to be created", required = true) CategoryDTO categoryDTO) {
        categoryService.saveCategory(userId, categoryDTO);
    }

    @GetMapping()
    @Operation(summary = "Get user categories", description = "Retrieves all categories for the specified user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No categories found for the user"),
            @ApiResponse(responseCode = "400", description = "Invalid path variable")
    })
    public ResponseEntity<List<CategoryDTO>> getCategories(@PathVariable @NotNull @Positive @Parameter(description = "User ID whose categories are to be retrieved", required = true) Long userId) {
        val result = categoryService.getCategories(userId);
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a category", description = "Updates an existing category for the specified user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or path variable"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void updateCategory(@PathVariable @NotNull @Positive @Parameter(description = "User ID for whom the category is being updated", required = true) Long userId,
                               @PathVariable @NotNull @Positive @Parameter(description = "Category ID to be updated", required = true) Long categoryId,
                               @RequestBody @Validated(ValidationExceptionHandler.OnUpdate.class) @Parameter(description = "Updated category details", required = true) CategoryDTO categoryDTO) {
        categoryService.updateCategory(userId, categoryId, categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category", description = "Deletes an existing category for the specified user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid path variable"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteCategory(@PathVariable @NotNull @Positive @Parameter(description = "User ID for whom the category is being deleted", required = true) Long userId,
                               @PathVariable @NotNull @Positive @Parameter(description = "Category ID to be deleted", required = true) Long categoryId) {
        categoryService.deleteCategory(userId, categoryId);
    }
}
