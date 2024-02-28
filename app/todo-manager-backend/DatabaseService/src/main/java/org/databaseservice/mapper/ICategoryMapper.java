package org.databaseservice.mapper;

import org.databaseservice.models.CategoryEntity;
import org.databaseservice.payload.CategoryDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ICategoryMapper {
    CategoryDTO categoryEntityToCategoryDto(CategoryEntity categoryEntity);
    CategoryEntity categoryDtoToCategoryEntity(CategoryDTO categoryDTO);
}
