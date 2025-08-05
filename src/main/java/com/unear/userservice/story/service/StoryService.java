package com.unear.userservice.story.service;

import com.unear.userservice.story.dto.request.StoryCreateRequestDto;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.dto.response.StoryDetailResponseDto;

import java.util.List;

public interface StoryService {

    StoryDiagnosisResponseDto getDiagnosis(Long userId);

    void createStory(Long userId, StoryCreateRequestDto request);

    List<StoryDetailResponseDto> getUserStories(Long userId, String targetMonth);
}
