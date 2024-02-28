package org.databaseservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.*;
import org.databaseservice.mapper.ICategoryMapper;
import org.databaseservice.models.CategoryEntity;
import org.databaseservice.payload.CategoryDTO;
import org.databaseservice.repository.ICategoryRepository;
import org.databaseservice.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;

    private final ICategoryMapper categoryMapper;

    @Override
    @Transactional
    public void saveCategory(Long userId, CategoryDTO categoryDTO) {
        val userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty())
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);

        try {
            var categoryEntity = new CategoryEntity(categoryDTO.getName(), userOptional.get());
            categoryRepository.save(categoryEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SaveException(ErrorMessage.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public List<CategoryDTO> getCategories(Long userId) {
        return categoryRepository.findCategoryEntitiesByUserId(userId)
                .stream()
                .map(categoryMapper::categoryEntityToCategoryDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateCategory(Long userId, CategoryDTO categoryDTO) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryDTO.getId());
        if(categoryEntityOptional.isEmpty())
            throw new NotFoundException(ErrorMessage.CATEGORY_MOT_FOUND);

        var categoryEntity = categoryEntityOptional.get();

        if(!categoryEntity.getUser().getId().equals(userId))
            throw new BadRequestException(ErrorMessage.PERMISSION_DENIED);

        try {
            categoryEntity.setName(categoryDTO.getName());
            categoryRepository.save(categoryEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UpdateException(ErrorMessage.UPDATE_ERROR);
        }
    }
}
