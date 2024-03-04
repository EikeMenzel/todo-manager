package org.databaseservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.*;
import org.databaseservice.mapper.ITodoMapper;
import org.databaseservice.models.TodoEntity;
import org.databaseservice.payload.ToDoDTO;
import org.databaseservice.repository.ITodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToDoService implements IToDoService {
    private final ValidationUtilsService validationUtilsService;
    private final ITodoRepository todoRepository;
    private final ITodoMapper todoMapper;

    @Override
    @Transactional
    public void saveTodo(Long userId, Long categoryId, ToDoDTO toDoDTO) {
        val category = validationUtilsService.fetchAndValidateCategory(toDoDTO.getCategoryId());

        validationUtilsService.validateUserAccessToCategory(userId, category);
        validationUtilsService.validateIdsMatch(categoryId, toDoDTO.getCategoryId());

        try {
            TodoEntity todoEntity = new TodoEntity(
                    toDoDTO.getText(),
                    category,
                    todoMapper.mapStatus(toDoDTO.getStatus()),
                    todoMapper.mapPriority(toDoDTO.getPriority())
            );
            todoRepository.save(todoEntity);
        } catch (Exception e) {
            log.error("Error saving ToDo: {}", e.getMessage(), e);
            throw new SaveException(ErrorMessage.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateTodo(Long userId, Long categoryId, Long toDoId, ToDoDTO toDoDTO) {
        val todoEntity = validationUtilsService.fetchAndValidateTodoEntity(toDoDTO.getId());

        validationUtilsService.validateUserAccessToCategory(userId, todoEntity.getCategory());
        validationUtilsService.validateIdsMatch(toDoId, toDoDTO.getId()); // categoryId and toDoDTO.categoryId() do not need to be checked, they are different, if the task was moved to another category
        validationUtilsService.validateIdsMatch(categoryId, todoEntity.getCategory().getId());

        if (!toDoId.equals(toDoDTO.getId()) || !categoryId.equals(todoEntity.getCategory().getId()))
            throw new BadRequestException(ErrorMessage.BAD_REQUEST_IDS_MISMATCH);

        val updatedTodoEntry = updateTodoEntity(todoEntity, toDoDTO);
        try {
            todoRepository.save(updatedTodoEntry);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UpdateException(ErrorMessage.UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteTodo(Long userId, Long categoryId, Long toDoId) {
        val todoEntity = validationUtilsService.fetchAndValidateTodoEntity(toDoId);

        validationUtilsService.validateUserAccessToCategory(userId, todoEntity.getCategory());
        validationUtilsService.validateIdsMatch(todoEntity.getCategory().getId(), categoryId);

        try {
            todoRepository.delete(todoEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DeleteException(ErrorMessage.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToDoDTO> getTodosFromCategory(Long userId, Long categoryId) {
        val category = validationUtilsService.fetchAndValidateCategory(categoryId);

        validationUtilsService.validateUserAccessToCategory(userId, category);

        return todoRepository.findTodoEntitiesByCategoryId(categoryId)
                .stream()
                .map(todoEntity -> new ToDoDTO(
                        todoEntity.getId(),
                        todoEntity.getText(),
                        todoMapper.mapStatusToDto(todoEntity.getStatus()),
                        todoMapper.mapPriorityToDto(todoEntity.getPriority()),
                        todoEntity.getCategory().getId()
                ))
                .toList();
    }

    private TodoEntity updateTodoEntity(TodoEntity todoEntity, ToDoDTO toDoDTO) {
        todoEntity.setText(toDoDTO.getText());
        todoEntity.setStatus(todoMapper.mapStatus(toDoDTO.getStatus()));
        todoEntity.setPriority(todoMapper.mapPriority(toDoDTO.getPriority()));

        if (!todoEntity.getCategory().getId().equals(toDoDTO.getCategoryId())) {
            val newCategory = validationUtilsService.fetchAndValidateCategory(toDoDTO.getCategoryId());

            validationUtilsService.validateUserAccessToCategory(todoEntity.getCategory().getUser().getId(), newCategory);

            todoEntity.setCategory(newCategory);
        }
        return todoEntity;
    }
}
