package org.databaseservice.repository;

import org.databaseservice.models.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findTodoEntitiesByCategoryId(Long categoryId);
}
