package org.databaseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.BadRequestException;
import org.databaseservice.payload.CategoryDTO;
import org.databaseservice.services.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/db/users/{userId}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@PathVariable Long userId, @RequestBody @Valid CategoryDTO categoryDTO) {
        categoryService.saveCategory(userId, categoryDTO);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getCategories(@PathVariable Long userId) {
        val result = categoryService.getCategories(userId);
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable Long userId, @PathVariable Long categoryId, @RequestBody @Valid CategoryDTO categoryDTO) {
        if(!categoryId.equals(categoryDTO.getId()))
            throw new BadRequestException(ErrorMessage.BAD_REQUEST_IDS_MISMATCH);

        categoryService.updateCategory(userId, categoryDTO);
    }
}
