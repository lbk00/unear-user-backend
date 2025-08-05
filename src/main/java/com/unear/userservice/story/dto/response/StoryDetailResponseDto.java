package com.unear.userservice.story.dto.response;

import com.unear.userservice.story.dto.request.StoryCreateRequestDto;
import com.unear.userservice.story.entity.Story;
import com.unear.userservice.story.entity.StoryRepresentativeLog;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record StoryDetailResponseDto(
        String imageUrl,
        String comment,
        LocalDate date,
        String storeName,
        int amount,
        String logoUrl
) {
    public static StoryDetailResponseDto of(Story story, StoryRepresentativeLog log) {
        return StoryDetailResponseDto.builder()
                .imageUrl(story.getImageUrl())
                .comment(story.getComment())
                .date(log.getDate())
                .storeName(log.getStoreName())
                .amount(log.getAmount())
                .logoUrl(log.getLogoUrl())
                .build();
    }
    public static StoryDetailResponseDto ofEmpty(Story story) {
        return StoryDetailResponseDto.builder()
                .imageUrl("BASIC.png")
                .comment("이번 달은 결제 내역이 없어..")
                .date(null)
                .storeName("결제 내역 없음")
                .amount(0)
                .logoUrl(null)
                .build();
    }


}
