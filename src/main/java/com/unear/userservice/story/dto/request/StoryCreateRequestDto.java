package com.unear.userservice.story.dto.request;

import lombok.Builder;

import java.time.LocalDate;
import java.time.YearMonth;

@Builder
public record StoryCreateRequestDto(
        YearMonth month,
        String type, // 찝찝박사 등
        String comment,
        String imageUrl,
        RepresentativeLog representativeLog,
        YearMonth targetMonth
) {
    @Builder
    public record RepresentativeLog(
            LocalDate date,
            String storeName,
            int amount,
            String logoUrl

    ) {}
}
