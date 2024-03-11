package org.taskservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Valid
@Tag(name = "Category Management", description = "API for managing categories")
public class CategoryController {
    private final IDatabaseServiceClient databaseServiceClient;
    @PostMapping()
    @Operation(summary = "Save a new category", description = "Saves a new category for a given user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters or body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> saveCategory(
            @RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
            @RequestBody @Valid @Validated(ValidationExceptionHandler.OnCreate.class) @Parameter(description = "Category details", required = true) CategoryDTO categoryDTO) {
        return databaseServiceClient.createCategory(userId, categoryDTO);
    }

    @GetMapping()
    @Operation(summary = "Get categories", description = "Retrieves all categories for a given user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No categories exist"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    })
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId) {
        return databaseServiceClient.getCategoryDTOList(userId);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category", description = "Updates an existing category for a given user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters or body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> updateCategory(
            @RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
            @PathVariable @NotNull @Positive @Parameter(description = "Category ID", required = true) Long categoryId,
            @RequestBody @Valid @Validated(ValidationExceptionHandler.OnUpdate.class) @Parameter(description = "Updated category details", required = true) CategoryDTO categoryDTO) {
        return databaseServiceClient.updateCategory(userId, categoryId, categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete a category", description = "Deletes an existing category for a given user", tags = { "Category Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCategory(
            @RequestHeader @NotNull @Positive @Parameter(description = "User ID", required = true) Long userId,
            @PathVariable @NotNull @Positive @Parameter(description = "Category ID", required = true) Long categoryId) {
        return databaseServiceClient.deleteCategory(userId, categoryId);
    }
}
