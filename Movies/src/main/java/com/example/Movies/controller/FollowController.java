package com.example.Movies.controller;

import com.example.Movies.dto.FollowResponseDTO;
import com.example.Movies.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // --- S'ABONNER ---
    // POST http://localhost:8080/api/follows/1/to/2
    @PostMapping("/{followerId}/to/{followingId}")
    public ResponseEntity<FollowResponseDTO> followUser(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {

        FollowResponseDTO response = followService.follow(followerId, followingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // --- SE DÉSABONNER ---
    // DELETE http://localhost:8080/api/follows/1/from/2
    @DeleteMapping("/{followerId}/from/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {

        followService.unfollow(followerId, followingId);
        return ResponseEntity.noContent().build(); // Retourne un code 204
    }

    // --- LISTE DES ABONNEMENTS ---
    // GET http://localhost:8080/api/follows/user/1
    @GetMapping("/user/{followerId}")
    public ResponseEntity<List<FollowResponseDTO>> getMyFollowingList(
            @PathVariable Long followerId) {

        List<FollowResponseDTO> list = followService.getFollowingList(followerId);
        return ResponseEntity.ok(list);
    }

    // --- COMPTER LES ABONNÉS ---
    // GET http://localhost:8080/api/follows/count/followers/1
    @GetMapping("/count/followers/{userId}")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowersCount(userId));
    }



    // Ajoutez ceci dans FollowController.java
    @GetMapping("/status/{followerId}/{followingId}")
    public ResponseEntity<Boolean> checkFollowStatus(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {
        // Appel au service ou repository pour vérifier l'existence
        boolean exists = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(exists);
    }
}
