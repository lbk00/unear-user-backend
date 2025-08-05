package com.unear.userservice.story.repository;

import com.unear.userservice.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    boolean existsByUserId(Long userId);
    boolean existsByUserIdAndTargetMonth(Long userId, String targetMonth);
    List<Story> findByUserIdAndTargetMonth(Long userId, String targetMonth);
}