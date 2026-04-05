package com.example.Movies.controller;

import com.example.Movies.dto.ProfileDTO;
import com.example.Movies.service.ProfileService;
import com.example.Movies.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService  userService;
    // Suppression de @RequestPart qui causait l'erreur 415
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDTO> saveProfile(
            @RequestParam("userId") Long userId,
            @RequestParam("username") String username,
            @RequestParam("bio") String bio,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) throws IOException {

        // On reconstruit un petit DTO pour le passer au service
        ProfileDTO dto = new ProfileDTO(null, username, bio, null, userId);

        return ResponseEntity.ok(profileService.createOrUpdateProfile(dto, avatar));
    }}