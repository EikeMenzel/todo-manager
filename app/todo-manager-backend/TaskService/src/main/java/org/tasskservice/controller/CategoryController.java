package org.tasskservice.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tasskservice.clients.IDatabaseServiceClient;
import org.tasskservice.payload.CategoryDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final IDatabaseServiceClient databaseServiceClient;
    @PostMapping()
    public ResponseEntity<Void> saveCategory(@RequestHeader @NotNull Long userId, @RequestBody @Validated CategoryDTO categoryDTO) {
        return databaseServiceClient.createCategory(userId, categoryDTO);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestHeader @NotNull Long userId) {
        return databaseServiceClient.getCategoryDTOList(userId);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> saveCategory(@RequestHeader @NotNull Long userId, @PathVariable @NotNull Long categoryId, @RequestBody @Validated CategoryDTO categoryDTO) {
        return databaseServiceClient.updateCategory(userId, categoryId, categoryDTO);
    }
}
