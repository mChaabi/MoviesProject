package com.example.Movies.controller;

import com.example.Movies.dto.TagDTO;
import com.example.Movies.dto.TagDetailsDTO;
import com.example.Movies.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Indispensable pour Angular
public class TagController {

    private final TagService tagService;

    // GET : http://localhost:8080/api/tags
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    // GET : http://localhost:8080/api/tags/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TagDetailsDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    // POST : http://localhost:8080/api/tags
    @PostMapping("/add")
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        TagDTO createdTag = tagService.createTag(tagDTO);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    // DELETE : http://localhost:8080/api/tags/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
