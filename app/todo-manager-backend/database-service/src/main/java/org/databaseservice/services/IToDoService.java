package org.databaseservice.services;

import org.databaseservice.payload.ToDoDTO;

import java.util.List;

public interface IToDoService {
    void saveTodo(Long userId, Long categoryId, ToDoDTO toDoDTO);
    void updateTodo(Long userId, Long categoryId, Long toDoId, ToDoDTO toDoDTO);
    void deleteTodo(Long userId, Long categoryId, Long toDoId);
    List<ToDoDTO> getTodosFromCategory(Long userId, Long categoryId);
}
