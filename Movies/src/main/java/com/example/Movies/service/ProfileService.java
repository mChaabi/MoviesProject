package com.example.Movies.service;

import com.example.Movies.dto.ProfileDTO;
import com.example.Movies.entity.Profile;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.ProfileMapper;
import com.example.Movies.repository.ProfileRepository;
import com.example.Movies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    private static final Path AVATAR_DIR = Paths.get("uploads", "avatars");

    @Transactional
    public ProfileDTO createOrUpdateProfile(ProfileDTO dto, MultipartFile avatarFile) throws IOException {

        // 1️⃣ Vérifier utilisateur
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2️⃣ Récupérer ou créer le profil
        Profile profile = profileRepository.findByUserId(dto.userId())
                .orElse(new Profile());

        profile.setUsername(dto.username());
        profile.setBio(dto.bio());
        profile.setUser(user);

        // 3️⃣ Sauvegarde avatar
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String fileName = saveAvatar(avatarFile);
            profile.setAvatarUrl("/uploads/avatars/" + fileName);
        }

        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toDTO(savedProfile);
    }

    /**
     * Création automatique du dossier + sauvegarde sécurisée
     */
    private String saveAvatar(MultipartFile file) throws IOException {

        // ✔ Crée le dossier s’il n’existe pas
        Files.createDirectories(AVATAR_DIR);

        // ✔ Nettoyage du nom du fichier (sécurité)
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // ✔ Nom unique
        String fileName = UUID.randomUUID() + "_" + originalFilename;

        Path targetPath = AVATAR_DIR.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return fileName;
    }
}
