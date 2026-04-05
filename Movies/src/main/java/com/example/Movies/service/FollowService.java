package com.example.Movies.service;

import com.example.Movies.dto.FollowResponseDTO;
import com.example.Movies.entity.Follow;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.FollowMapper;
import com.example.Movies.repository.FollowRepository;
import com.example.Movies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FollowMapper followMapper;

    // --- POST : S'abonner à un utilisateur ---
    public FollowResponseDTO follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("Vous ne pouvez pas vous suivre vous-même");
        }

        // Vérifier si l'abonnement existe déjà
        followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .ifPresent(f -> { throw new RuntimeException("Déjà abonné"); });

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower non trouvé"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User à suivre non trouvé"));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        // La date est gérée par @CreationTimestamp dans l'entité si présent

        return followMapper.toResponseDTO(followRepository.save(follow));
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        // On utilise le repository pour voir si une ligne existe avec ces deux IDs
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId).isPresent();
    }

    // --- DELETE : Se désabonner ---
    public void unfollow(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new RuntimeException("Abonnement inexistant"));

        followRepository.delete(follow);
    }

    // --- GET : Récupérer la liste des abonnements d'un utilisateur ---
    public List<FollowResponseDTO> getFollowingList(Long followerId) {
        List<Follow> follows = followRepository.findByFollowerId(followerId);
        return followMapper.toResponseDTOList(follows);
    }

    // --- GET : Statistiques (Optionnel) ---
    public long getFollowersCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
}