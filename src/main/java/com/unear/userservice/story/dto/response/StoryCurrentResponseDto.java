package com.unear.userservice.story.dto.response;

import lombok.Builder;

import java.time.YearMonth;
import java.time.LocalDate;

@Builder
public record StoryCurrentResponseDto(
        YearMonth month,
        String comment,
        String imageUrl,
        RepresentativeLog representativeLog
) {
    @Builder
    public record RepresentativeLog(
            LocalDate date,
            String storeName,
            int amount,
            String logoUrl
    ) {}
}