package com.unear.userservice.stamp.dto.response;

import com.unear.userservice.stamp.entity.Stamp;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StampResponseDto {
    private Long stampId;
    private String placeName;       // 예: 성수동 팝업스토어
    private String eventCode;       // 예: REQUIRE, GENERAL
    private LocalDateTime stampedAt;

    public static StampResponseDto from(Stamp stamp) {
        return StampResponseDto.builder()
                .stampId(stamp.getStampId())
                .placeName(stamp.getPlaceName())
                .eventCode(stamp.getEventCode())
                .stampedAt(stamp.getStampedAt())
                .build();
    }
}
