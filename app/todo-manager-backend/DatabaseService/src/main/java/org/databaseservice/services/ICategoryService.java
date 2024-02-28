package org.databaseservice.services;

import org.databaseservice.payload.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    void saveCategory(Long userId, CategoryDTO categoryDTO);
    List<CategoryDTO> getCategories(Long userId);
    void updateCategory(Long userId, CategoryDTO categoryDTO);
}
