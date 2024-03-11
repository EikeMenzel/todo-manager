package org.databaseservice.service;

import org.databaseservice.mapper.ICategoryMapper;
import org.databaseservice.models.*;
import org.databaseservice.payload.CategoryDTO;
import org.databaseservice.payload.ToDoDTO;
import org.databaseservice.payload.ToDoPriorityDTO;
import org.databaseservice.payload.ToDoStatusDTO;
import org.databaseservice.repository.ICategoryRepository;
import org.databaseservice.repository.IUserRepository;
import org.databaseservice.services.CategoryService;
import org.databaseservice.services.ValidationUtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private ValidationUtilsService validationUtilsService;

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ICategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private final Long validUserId = 1L;
    private final Long validCategoryId = 2L;
    private CategoryEntity validCategoryEntity;
    private UserEntity validUserEntity;
    @BeforeEach
    void setUp() {
        validUserEntity = new UserEntity(validUserId, "testUser", "testPassword");
        this.validCategoryEntity = new CategoryEntity(validCategoryId, "NewCategory", validUserEntity);
    }

    @Test
    void saveCategory_Success_ShouldSaveCategory() {
        Long userId = 1L;
        String categoryName = "NewCategory";
        CategoryDTO categoryDTO = new CategoryDTO(null, categoryName);

        when(validationUtilsService.fetchAndValidateUser(userId)).thenReturn(validUserEntity);

        ArgumentCaptor<CategoryEntity> categoryEntityCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

        categoryService.saveCategory(userId, categoryDTO);

        verify(categoryRepository).save(categoryEntityCaptor.capture());

        CategoryEntity savedCategoryEntity = categoryEntityCaptor.getValue();
        assertNotNull(savedCategoryEntity);
        assertEquals(categoryName, savedCategoryEntity.getName());
        assertEquals(validUserEntity, savedCategoryEntity.getUser());
    }

    @Test
    void getCategories_Success_ShouldReturnCategories() {
        Long userId = 1L;
        List<CategoryEntity> categories = List.of(validCategoryEntity);
        CategoryDTO categoryDTO = new CategoryDTO(1L, "NewCategory");

        when(validationUtilsService.fetchAndValidateUser(userId)).thenReturn(validUserEntity);
        when(categoryRepository.findCategoryEntitiesByUserId(userId)).thenReturn(categories);
        when(categoryMapper.categoryEntityToCategoryDto(Mockito.<CategoryEntity>any())).thenReturn(categoryDTO);

        List<CategoryDTO> result = categoryService.getCategories(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(categories.size(), result.size());
    }

    @Test
    void updateCategory_Success_ShouldUpdateCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "NewCategory");

        when(validationUtilsService.fetchAndValidateCategory(categoryDTO.getId())).thenReturn(validCategoryEntity);
        doNothing().when(validationUtilsService).validateUserAccessToCategory(validUserId, validCategoryEntity);
        doNothing().when(validationUtilsService).validateIdsMatch(validCategoryId, categoryDTO.getId());

        when(categoryRepository.save(validCategoryEntity)).thenReturn(validCategoryEntity);

        assertDoesNotThrow(() -> categoryService.updateCategory(validUserId, validCategoryId, categoryDTO));
        verify(categoryRepository, times(1)).save(validCategoryEntity);
        assertEquals(categoryDTO.getName(), validCategoryEntity.getName());
    }

    @Test
    void deleteCategory_Success_ShouldDeleteCategory() {
        when(validationUtilsService.fetchAndValidateCategory(validCategoryId)).thenReturn(validCategoryEntity);
        doNothing().when(validationUtilsService).validateUserAccessToCategory(validUserId, validCategoryEntity);

        categoryService.deleteCategory(validUserId, validCategoryId);

        verify(categoryRepository).delete(validCategoryEntity);
    }

}
