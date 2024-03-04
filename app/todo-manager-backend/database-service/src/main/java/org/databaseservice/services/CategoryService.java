package org.databaseservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.*;
import org.databaseservice.mapper.ICategoryMapper;
import org.databaseservice.models.CategoryEntity;
import org.databaseservice.payload.CategoryDTO;
import org.databaseservice.repository.ICategoryRepository;
import org.databaseservice.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {
    private final ValidationUtilsService validationUtilsService;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;

    private final ICategoryMapper categoryMapper;

    @Override
    @Transactional
    public void saveCategory(Long userId, CategoryDTO categoryDTO) {
        val user = validationUtilsService.fetchAndValidateUser(userId);

        try {
            var categoryEntity = new CategoryEntity(categoryDTO.getName(), user);
            categoryRepository.save(categoryEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SaveException(ErrorMessage.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public List<CategoryDTO> getCategories(Long userId) {
        validationUtilsService.fetchAndValidateUser(userId); // check if user exists

        return categoryRepository.findCategoryEntitiesByUserId(userId)
                .stream()
                .map(categoryMapper::categoryEntityToCategoryDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateCategory(Long userId, Long categoryId, CategoryDTO categoryDTO) {
        validationUtilsService.validateIdsMatch(categoryId, categoryDTO.getId());

        val category = validationUtilsService.fetchAndValidateCategory(categoryDTO.getId());

        validationUtilsService.validateUserAccessToCategory(userId, category);

        try {
            category.setName(categoryDTO.getName());
            categoryRepository.save(category);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UpdateException(ErrorMessage.UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        val category = validationUtilsService.fetchAndValidateCategory(categoryId);

        validationUtilsService.validateUserAccessToCategory(userId, category);

        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UpdateException(ErrorMessage.DELETE_ERROR);
        }
    }
}
