package com.example.Movies.mapper;

import com.example.Movies.dto.CategoryDTO;
import com.example.Movies.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;
        return Category.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .build();
    }
}