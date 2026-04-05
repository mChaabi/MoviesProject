package com.example.Movies.service;

import com.example.Movies.dto.FollowResponseDTO;
import com.example.Movies.entity.Follow;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.FollowMapper;
import com.example.Movies.repository.FollowRepository;
import com.example.Movies.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Permet d'utiliser les annotations Mockito
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowMapper followMapper;

    @InjectMocks
    private FollowService followService; // Injecte les mocks ci-dessus dans le service

    private User follower;
    private User following;
    private Follow follow;

    @BeforeEach
    void setUp() {
        follower = User.builder().id(1L).email("follower@test.com").build();
        following = User.builder().id(2L).email("following@test.com").build();

        follow = new Follow();
        follow.setId(100L);
        follow.setFollower(follower);
        follow.setFollowing(following);
        follow.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void follow_ShouldSucceed_WhenValidIds() {
        // GIVEN
        when(followRepository.findByFollowerIdAndFollowingId(1L, 2L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(following));
        when(followRepository.save(any(Follow.class))).thenReturn(follow);
        when(followMapper.toResponseDTO(any(Follow.class)))
                .thenReturn(new FollowResponseDTO(100L, 2L, "following@test.com", LocalDateTime.now()));

        // WHEN
        FollowResponseDTO result = followService.follow(1L, 2L);

        // THEN
        assertNotNull(result);
        assertEquals("following@test.com", result.email());
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    void follow_ShouldThrowException_WhenSelfFollowing() {
        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            followService.follow(1L, 1L);
        });

        assertEquals("Vous ne pouvez pas vous suivre vous-même", exception.getMessage());
        verify(followRepository, never()).save(any());
    }

    @Test
    void unfollow_ShouldCallDelete_WhenFollowExists() {
        // GIVEN
        when(followRepository.findByFollowerIdAndFollowingId(1L, 2L)).thenReturn(Optional.of(follow));

        // WHEN
        followService.unfollow(1L, 2L);

        // THEN
        verify(followRepository, times(1)).delete(follow);
    }

    @Test
    void unfollow_ShouldThrowException_WhenFollowDoesNotExist() {
        // GIVEN
        when(followRepository.findByFollowerIdAndFollowingId(1L, 2L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> followService.unfollow(1L, 2L));
    }
}
