package com.example.Movies.service;

import com.example.Movies.dto.TagDTO;
import com.example.Movies.dto.TagDetailsDTO;
import com.example.Movies.entity.Tag;
import com.example.Movies.mapper.TagMapper;
import com.example.Movies.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    private Tag tagEntity;
    private TagDTO tagDTO;

    @BeforeEach
    void setUp() {
        // L'entité reste une classe classique avec des setters
        tagEntity = new Tag();
        tagEntity.setId(1L);
        tagEntity.setLabel("Action");

        // Le DTO est un record : on utilise le constructeur
        tagDTO = new TagDTO(1L, "Action");
    }

    @Test
    void getAllTags_ShouldReturnList() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(Arrays.asList(tagEntity));
        when(tagMapper.toDTO(tagEntity)).thenReturn(tagDTO);

        // Act
        List<TagDTO> result = tagService.getAllTags();

        // Assert
        assertThat(result).hasSize(1);
        // Correction : result.get(0).label() au lieu de getLabel()
        assertThat(result.get(0).label()).isEqualTo("Action");
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void getTagById_WhenExists_ShouldReturnDetails() {
        // Arrange
        // Record : initialisation via constructeur avec un Set vide pour les movies
        TagDetailsDTO detailsDTO = new TagDetailsDTO(1L, "Action", java.util.Collections.emptySet());

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tagEntity));
        when(tagMapper.toDetailsDTO(tagEntity)).thenReturn(detailsDTO);

        // Act
        TagDetailsDTO result = tagService.getTagById(1L);

        // Assert
        assertThat(result).isNotNull();
        // Correction : accès via label()
        assertThat(result.label()).isEqualTo("Action");
    }

    @Test
    void createTag_ShouldSaveAndReturnDTO() {
        // Arrange
        when(tagMapper.toEntity(any(TagDTO.class))).thenReturn(tagEntity);
        when(tagRepository.save(any(Tag.class))).thenReturn(tagEntity);
        when(tagMapper.toDTO(tagEntity)).thenReturn(tagDTO);

        // Act
        TagDTO result = tagService.createTag(tagDTO);

        // Assert
        assertThat(result).isNotNull();
        // Correction : accès via label()
        assertThat(result.label()).isEqualTo("Action");
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void deleteTag_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(tagRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        // Vérifie que l'exception RuntimeException est bien lancée
        assertThrows(RuntimeException.class, () -> tagService.deleteTag(99L));
        verify(tagRepository, never()).deleteById(anyLong());
    }
}