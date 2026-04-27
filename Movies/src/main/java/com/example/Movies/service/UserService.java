package com.example.Movies.service;

import com.example.Movies.dto.UserDTO;
import com.example.Movies.entity.Role;           // ✅ Import correct
import com.example.Movies.entity.User;
import com.example.Movies.mapper.UserMapper;
import com.example.Movies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // ✅ NOUVEAU — Méthode login
    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifie le mot de passe haché
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return userMapper.toDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password())) // 🔥
                .role(dto.role() != null ? dto.role() : Role.USER) // 🔥
                .build();

        return mapToDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mise à jour impossible : ID inconnu"));

        existingUser.setEmail(userDTO.email());
        if (userDTO.password() != null && !userDTO.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        return userMapper.toDTO(userRepository.save(existingUser));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Suppression impossible : ID " + id + " n'existe pas");
        }
        userRepository.deleteById(id);
    }

    // Méthode utilitaire privée (si tu l'utilises)
    private UserDTO mapToDTO(User user) {
        return userMapper.toDTO(user);
    }
}