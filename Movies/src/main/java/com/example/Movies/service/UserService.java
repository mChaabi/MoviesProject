package com.example.Movies.service;

import com.example.Movies.dto.UserDTO;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.UserMapper;
import com.example.Movies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // --- GET ALL (Récupérer tout le monde) ---
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- GET BY ID (Récupérer un seul utilisateur) ---
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
        return userMapper.toDTO(user);
    }

    // --- POST (Créer un utilisateur avec mot de passe haché) ---
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        // Sécurité : on remplace le mot de passe en clair par sa version hachée (BCrypt)
        user.setPassword(passwordEncoder.encode(userDTO.password()));

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // --- PUT (Mettre à jour un utilisateur) ---
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mise à jour impossible : ID inconnu"));

        // Mise à jour des informations de base
        existingUser.setEmail(userDTO.email());

        // Si un nouveau mot de passe est envoyé, on le re-hache
        if (userDTO.password() != null && !userDTO.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    // --- DELETE (Supprimer un utilisateur) ---
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Suppression impossible : ID " + id + " n'existe pas");
        }
        userRepository.deleteById(id);
    }
}