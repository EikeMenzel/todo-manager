package org.databaseservice.service;

import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.DeleteException;
import org.databaseservice.exceptions.SaveException;
import org.databaseservice.exceptions.UpdateException;
import org.databaseservice.mapper.ITodoMapper;
import org.databaseservice.models.*;
import org.databaseservice.payload.ToDoDTO;
import org.databaseservice.payload.ToDoPriorityDTO;
import org.databaseservice.payload.ToDoStatusDTO;
import org.databaseservice.repository.ITodoRepository;
import org.databaseservice.services.ToDoService;
import org.databaseservice.services.ValidationUtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

    @Mock
    private ValidationUtilsService validationUtilsService;

    @Mock
    private ITodoRepository todoRepository;

    @Mock
    private ITodoMapper todoMapper;

    @InjectMocks
    private ToDoService toDoService;

    private final Long validUserId = 1L;
    private final Long validCategoryId = 2L;
    private final Long validTodoId = 3L;
    private ToDoDTO validToDoDTO;
    private TodoEntity validTodoEntity;
    private CategoryEntity validCategory;
    private UserEntity userEntity;
    @BeforeEach
    void setUp() {
        userEntity = new UserEntity(validUserId, "testUser", "testPassword");
        this.validCategory = new CategoryEntity(validCategoryId, "NewCategory", userEntity);
        this.validTodoEntity = new TodoEntity(validTodoId, "TodoTask", validCategory, TodoStatus.NOT_STARTED, TodoPriority.LOW);
        this.validToDoDTO = new ToDoDTO(validTodoId, "TodoTask", ToDoStatusDTO.NOT_STARTED, ToDoPriorityDTO.LOW, validCategoryId);
    }

    @Test
    void saveTodo_Success_ShouldSaveTodo() {
        when(validationUtilsService.fetchAndValidateCategory(validCategoryId)).thenReturn(validCategory);
        doNothing().when(validationUtilsService).validateUserAccessToCategory(validUserId, validCategory);
        doNothing().when(validationUtilsService).validateIdsMatch(validCategoryId, validToDoDTO.getCategoryId());

        toDoService.saveTodo(validUserId, validCategoryId, validToDoDTO);

        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void saveTodo_Exception_ShouldThrowSaveException() {
        when(validationUtilsService.fetchAndValidateCategory(validCategoryId)).thenReturn(validCategory);
        doThrow(new SaveException(ErrorMessage.SAVE_ERROR)).when(todoRepository).save(any(TodoEntity.class));

        assertThrows(SaveException.class, () -> toDoService.saveTodo(validUserId, validCategoryId, validToDoDTO));
    }

    @Test
    void updateTodo_Success_ShouldUpdateTodo() {
        when(validationUtilsService.fetchAndValidateTodoEntity(validTodoId)).thenReturn(validTodoEntity);
        doNothing().when(validationUtilsService).validateUserAccessToCategory(validUserId, validCategory);
        doNothing().when(validationUtilsService).validateIdsMatch(validTodoId, validToDoDTO.getId());

        toDoService.updateTodo(validUserId, validCategoryId, validTodoId, validToDoDTO);

        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void updateTodo_Exception_ShouldThrowUpdateException() {
        when(validationUtilsService.fetchAndValidateTodoEntity(validTodoId)).thenReturn(validTodoEntity);
        doThrow(new UpdateException(ErrorMessage.UPDATE_ERROR)).when(todoRepository).save(any(TodoEntity.class));

        assertThrows(UpdateException.class, () -> toDoService.updateTodo(validUserId, validCategoryId, validTodoId, validToDoDTO));
    }

    @Test
    void deleteTodo_Success_ShouldDeleteTodo() {
        when(validationUtilsService.fetchAndValidateTodoEntity(validTodoId)).thenReturn(validTodoEntity);
        doNothing().when(validationUtilsService).validateUserAccessToCategory(validUserId, validTodoEntity.getCategory());
        doNothing().when(validationUtilsService).validateIdsMatch(validTodoEntity.getCategory().getId(), validCategoryId);

        toDoService.deleteTodo(validUserId, validCategoryId, validTodoId);

        verify(todoRepository, times(1)).delete(validTodoEntity);
    }

    @Test
    void deleteTodo_Exception_ShouldThrowDeleteException() {
        when(validationUtilsService.fetchAndValidateTodoEntity(validTodoId)).thenReturn(validTodoEntity);
        doThrow(new DeleteException(ErrorMessage.DELETE_ERROR)).when(todoRepository).delete(validTodoEntity);

        assertThrows(DeleteException.class, () -> toDoService.deleteTodo(validUserId, validCategoryId, validTodoId));
    }

    @Test
    void getTodosFromCategory_ShouldReturnTodos() {
        when(validationUtilsService.fetchAndValidateCategory(validCategoryId)).thenReturn(validCategory);
        when(todoRepository.findTodoEntitiesByCategoryId(validCategoryId)).thenReturn(List.of(validTodoEntity));
        when(todoMapper.mapStatusToDto(any())).thenReturn(ToDoStatusDTO.FINISHED);
        when(todoMapper.mapPriorityToDto(any())).thenReturn(ToDoPriorityDTO.HIGH);

        List<ToDoDTO> result = toDoService.getTodosFromCategory(validUserId, validCategoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
