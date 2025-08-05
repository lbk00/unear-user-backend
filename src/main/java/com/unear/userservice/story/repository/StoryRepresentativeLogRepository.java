package com.unear.userservice.story.repository;

import com.unear.userservice.story.entity.Story;
import com.unear.userservice.story.entity.StoryRepresentativeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoryRepresentativeLogRepository extends JpaRepository<StoryRepresentativeLog, Long> {

    // 특정 스토리(summary) ID에 해당하는 대표 결제 내역들 조회
    List<StoryRepresentativeLog> findByStorySummaryId(Long storySummaryId);

    Optional<StoryRepresentativeLog> findByStorySummary(Story story);
}
