package com.unear.userservice.story.repository;

import com.unear.userservice.story.entity.UserRecommendationProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRecommendationProfileRepository extends JpaRepository<UserRecommendationProfile, Long> {
    Optional<UserRecommendationProfile> findByUserId(Long userId);
}
