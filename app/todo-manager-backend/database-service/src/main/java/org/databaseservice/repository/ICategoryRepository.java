package org.databaseservice.repository;

import org.databaseservice.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findCategoryEntitiesByUserId(Long userId);
}
