package com.unear.userservice.story.dto.response;

import com.unear.userservice.story.entity.Story;
import com.unear.userservice.story.entity.StoryRepresentativeLog;
import com.unear.userservice.user.entity.UserHistory;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record StoryCurrentResponseDto(
        String month, // YearMonth → String 으로 변경
        List<String> comment,
        String imageUrl,
        RepresentativeLog representativeLog
) {
    @Builder
    public record RepresentativeLog(
            LocalDate date,
            String storeName,
            int amount,
            String placeCategory // 마커 매핑용 값
    ) {}

    public static StoryCurrentResponseDto from(Story story, StoryRepresentativeLog log, UserHistory history) {
        return StoryCurrentResponseDto.builder()
                .month(story.getTargetMonth()) // String
                .comment(List.of(story.getComment().split("\n")))
                .imageUrl(story.getImageUrl())
                .representativeLog(RepresentativeLog.builder()
                        .date(log.getDate())
                        .storeName(log.getStoreName())
                        .amount(log.getAmount())
                        .placeCategory(history.getPlaceCategory())
                        .build())
                .build();
    }
}
