package com.unear.userservice.story.dto.response;

import lombok.Builder;

@Builder
public record StoryDiagnosisResponseDto(
        String type,
        String tag,
        boolean hasStory
) {}