package com.unear.userservice.story.dto.response;

import lombok.Builder;

import java.time.YearMonth;

@Builder
public record StoryHistoryResponseDto(
        YearMonth month,
        String type,
        String comment,
        String imageUrl
) {}