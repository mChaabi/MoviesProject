package com.example.Movies.service;

import com.example.Movies.dto.UserDTO;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.UserMapper;
import com.example.Movies.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Utilise Mockito pour Java 21 / JUnit 5
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Données de test communes
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .createdAt(LocalDateTime.now())
                .build();

        userDTO = new UserDTO(1L, "test@example.com", "rawPassword", LocalDateTime.now());
    }

    // --- SCÉNARIOS GET ALL ---

    @Test
    @DisplayName("Devrait retourner une liste de DTOs")
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo(userDTO.email());
        verify(userRepository, times(1)).findAll();
    }

    // --- SCÉNARIOS GET BY ID ---

    @Test
    @DisplayName("Devrait retourner un DTO quand l'ID existe")
    void getUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Devrait lancer une exception quand l'ID n'existe pas")
    void getUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utilisateur non trouvé");
    }

    // --- SCÉNARIOS CREATE (POST) ---

    @Test
    @DisplayName("Devrait encoder le password et sauvegarder l'utilisateur")
    void createUser_Success() {
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertThat(result).isNotNull();
        verify(passwordEncoder).encode("rawPassword"); // Vérifie que le hachage a eu lieu
        verify(userRepository).save(any(User.class));
    }

    // --- SCÉNARIOS UPDATE (PUT) ---

    @Test
    @DisplayName("Devrait mettre à jour l'email et le password si fourni")
    void updateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1L, userDTO);

        assertThat(result).isNotNull();
        verify(userRepository).save(user);
    }

    // --- SCÉNARIOS DELETE ---

    @Test
    @DisplayName("Devrait supprimer l'utilisateur si l'ID existe")
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Devrait échouer lors de la suppression d'un ID inexistant")
    void deleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Suppression impossible");
    }
}