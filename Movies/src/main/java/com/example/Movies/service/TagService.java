package com.example.Movies.service;

import com.example.Movies.dto.TagDTO;
import com.example.Movies.dto.TagDetailsDTO;
import com.example.Movies.entity.Tag;
import com.example.Movies.mapper.TagMapper;
import com.example.Movies.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Pour les logs
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TagDetailsDTO getTagById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toDetailsDTO)
                .orElseThrow(() -> {
                    log.error("Échec récupération : Tag {} inexistant", id);
                    return new RuntimeException("Tag introuvable avec l'ID : " + id);
                });
    }

    @Transactional
    public TagDTO createTag(TagDTO tagDTO) {
        try {
            Tag tag = tagMapper.toEntity(tagDTO);
            return tagMapper.toDTO(tagRepository.save(tag));
        } catch (Exception e) {
            log.error("Erreur lors de la création du tag : {}", e.getMessage());
            throw new RuntimeException("Erreur de sauvegarde : le label est peut-être déjà utilisé.");
        }
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            log.warn("Tentative de suppression d'un tag inexistant : {}", id);
            throw new RuntimeException("Suppression impossible : Tag " + id + " introuvable.");
        }
        try {
            tagRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Erreur suppression tag {} : {}", id, e.getMessage());
            throw new RuntimeException("Le tag ne peut pas être supprimé (il est probablement utilisé par des films).");
        }
    }
}