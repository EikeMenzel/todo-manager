package org.databaseservice.services;

import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.BadRequestException;
import org.databaseservice.exceptions.NotFoundException;
import org.databaseservice.models.CategoryEntity;
import org.databaseservice.models.TodoEntity;
import org.databaseservice.models.UserEntity;
import org.databaseservice.repository.ICategoryRepository;
import org.databaseservice.repository.ITodoRepository;
import org.databaseservice.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationUtilsService implements IValidationUtilsService {
    private final IUserRepository userRepository;
    private final ITodoRepository todoRepository;
    private final ICategoryRepository categoryRepository;

    public CategoryEntity fetchAndValidateCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CATEGORY_MOT_FOUND));
    }

    public TodoEntity fetchAndValidateTodoEntity(Long toDoId) {
        return todoRepository.findById(toDoId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.TODO_NOT_FOUND));
    }

    public void validateUserAccessToCategory(Long userId, CategoryEntity category) {
        if (!category.getUser().getId().equals(userId)) {
            throw new BadRequestException(ErrorMessage.PERMISSION_DENIED);
        }
    }

    public void validateIdsMatch(Long pathId, Long dtoId) {
        if (!pathId.equals(dtoId)) {
            throw new BadRequestException(ErrorMessage.BAD_REQUEST_IDS_MISMATCH);
        }
    }

    public UserEntity fetchAndValidateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

}
