package com.example.Movies.service;

import com.example.Movies.dto.CategoryDTO;
import com.example.Movies.dto.MovieDTO;
import com.example.Movies.entity.Category;
import com.example.Movies.entity.Movie;
import com.example.Movies.mapper.CategoryMapper;
import com.example.Movies.mapper.MovieMapper;
import com.example.Movies.repository.CategoryRepository;
import com.example.Movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    public List<MovieDTO> getMoviesByCategoryId(Long categoryId) {
        // UTILISEZ BIEN movieRepository ici
        List<Movie> movies = movieRepository.findByCategoryId(categoryId);
        return movieMapper.toDtoList(movies);
    }


    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Catégorie introuvable");
        }
        categoryRepository.deleteById(id);
    }
}