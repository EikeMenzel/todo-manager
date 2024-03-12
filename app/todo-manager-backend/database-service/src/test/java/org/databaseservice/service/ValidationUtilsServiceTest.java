package org.databaseservice.service;

import org.databaseservice.exceptions.BadRequestException;
import org.databaseservice.exceptions.NotFoundException;
import org.databaseservice.models.*;
import org.databaseservice.repository.ICategoryRepository;
import org.databaseservice.repository.ITodoRepository;
import org.databaseservice.repository.IUserRepository;
import org.databaseservice.services.ValidationUtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationUtilsServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITodoRepository todoRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private ValidationUtilsService validationUtilsService;

    private UserEntity mockUser;
    private TodoEntity mockTodo;
    private CategoryEntity mockCategory;

    private final Long validUserId = 1L;
    private final Long validTodoId = 2L;
    private final Long validCategoryId = 3L;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity(validUserId, "testUser", "testPassword");

        mockCategory = new CategoryEntity(validCategoryId, "Xyz", mockUser);

        mockTodo = new TodoEntity(validTodoId, "TestTodo", mockCategory, TodoStatus.NOT_STARTED, TodoPriority.LOW);
    }

    @Test
    void fetchAndValidateCategory_CategoryFound_ShouldReturnCategory() {
        Mockito.when(categoryRepository.findById(validCategoryId)).thenReturn(Optional.of(mockCategory));

        CategoryEntity result = validationUtilsService.fetchAndValidateCategory(validCategoryId);

        assertEquals(mockCategory, result);
    }

    @Test
    void fetchAndValidateCategory_CategoryNotFound_ShouldThrowNotFoundException() {
        Mockito.when(categoryRepository.findById(validCategoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> validationUtilsService.fetchAndValidateCategory(validCategoryId));
    }

    @Test
    void fetchAndValidateTodoEntity_TodoFound_ShouldReturnTodo() {
        Mockito.when(todoRepository.findById(validTodoId)).thenReturn(Optional.of(mockTodo));

        TodoEntity result = validationUtilsService.fetchAndValidateTodoEntity(validTodoId);

        assertEquals(mockTodo, result);
    }

    @Test
    void fetchAndValidateTodoEntity_TodoNotFound_ShouldThrowNotFoundException() {
        Mockito.when(todoRepository.findById(validTodoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> validationUtilsService.fetchAndValidateTodoEntity(validTodoId));
    }

    @Test
    void validateUserAccessToCategory_UserHasAccess_ShouldNotThrowException() {
        assertDoesNotThrow(() -> validationUtilsService.validateUserAccessToCategory(validUserId, mockCategory));
    }

    @Test
    void validateUserAccessToCategory_UserDoesNotHaveAccess_ShouldThrowBadRequestException() {
        Long anotherUserId = 99L;

        assertThrows(BadRequestException.class, () -> validationUtilsService.validateUserAccessToCategory(anotherUserId, mockCategory));
    }

    @Test
    void validateIdsMatch_IDsMatch_ShouldNotThrowException() {
        assertDoesNotThrow(() -> validationUtilsService.validateIdsMatch(validUserId, validUserId));
    }

    @Test
    void validateIdsMatch_IDsDoNotMatch_ShouldThrowBadRequestException() {
        Long mismatchedId = 99L;

        assertThrows(org.databaseservice.exceptions.BadRequestException.class, () -> validationUtilsService.validateIdsMatch(validUserId, mismatchedId));
    }

    @Test
    void fetchAndValidateUser_UserFound_ShouldReturnUser() {
        Mockito.when(userRepository.findById(validUserId)).thenReturn(Optional.of(mockUser));

        UserEntity result = validationUtilsService.fetchAndValidateUser(validUserId);

        assertEquals(mockUser, result);
    }

    @Test
    void fetchAndValidateUser_UserNotFound_ShouldThrowNotFoundException() {
        Mockito.when(userRepository.findById(validUserId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> validationUtilsService.fetchAndValidateUser(validUserId));
    }
}
