package com.example.Movies.controller;

import com.example.Movies.dto.CategoryDTO;
import com.example.Movies.dto.MovieDTO;
import com.example.Movies.service.CategoryService;
import com.example.Movies.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCategory(@PathVariable Long categoryId) {
        // CORRECT : vous utilisez le service où vous venez d'ajouter le code
        return ResponseEntity.ok(categoryService.getMoviesByCategoryId(categoryId));
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
