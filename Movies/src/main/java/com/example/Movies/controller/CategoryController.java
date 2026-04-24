package com.example.Movies.controller;

import com.example.Movies.dto.CategoryDTO;
import com.example.Movies.dto.MovieDTO;
import com.example.Movies.entity.Movie.MediaType;
import com.example.Movies.service.CategoryService;
import com.example.Movies.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ✅ MODIFIÉ — Ajout du filtre par type dans les catégories
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;
    private final MovieService movieService;

    // ✅ EXISTANT — inchangé
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // ✅ EXISTANT — inchangé
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getMoviesByCategoryId(categoryId));
    }

    // 🆕 NOUVEAU — Filtrer par catégorie et type (MOVIE ou SERIES)
    @GetMapping("/category/{categoryId}/type/{type}")
    public ResponseEntity<List<MovieDTO>> getContentByCategoryAndType(
            @PathVariable Long categoryId,
            @PathVariable MediaType type) {
        return ResponseEntity.ok(categoryService.getContentByCategoryAndType(categoryId, type));
    }

    // ✅ EXISTANT — inchangé
    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    // ✅ EXISTANT — inchangé
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
