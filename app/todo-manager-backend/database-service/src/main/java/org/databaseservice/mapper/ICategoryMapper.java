package org.databaseservice.mapper;

import org.databaseservice.models.CategoryEntity;
import org.databaseservice.payload.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
    CategoryDTO categoryEntityToCategoryDto(CategoryEntity categoryEntity);
}
