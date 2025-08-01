package com.unear.userservice.story.service;

import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;

public interface StoryService {
    StoryDiagnosisResponseDto getDiagnosis(Long userId);
}

