package org.databaseservice.services;

import org.databaseservice.models.CategoryEntity;
import org.databaseservice.models.TodoEntity;
import org.databaseservice.models.UserEntity;

public interface IValidationUtilsService {
    CategoryEntity fetchAndValidateCategory(Long categoryId);
    TodoEntity fetchAndValidateTodoEntity(Long toDoId);
    void validateUserAccessToCategory(Long userId, CategoryEntity category);
    void validateIdsMatch(Long pathId, Long dtoId);
    UserEntity fetchAndValidateUser(Long userId);
}
