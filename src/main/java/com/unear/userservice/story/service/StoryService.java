package com.unear.userservice.story.service;

import com.unear.userservice.story.dto.response.StoryCurrentResponseDto;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.dto.response.StoryHistoryResponseDto;

import java.util.List;

public interface StoryService {
    StoryDiagnosisResponseDto getDiagnosis(Long userId);
    List<StoryHistoryResponseDto> getHistory(Long userId);
    StoryCurrentResponseDto getCurrent(Long userId);
}

