package com.unear.userservice.stamp.dto.response;

import com.unear.userservice.stamp.entity.Stamp;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StampResponseDto {
    private Long eventPlaceId;
    private String placeName;
    private String eventCode;
    private boolean stamped;

    public static StampResponseDto from(Stamp stamp) {
        return StampResponseDto.builder()
                .eventPlaceId(stamp.getEventPlace().getEventPlaceId())
                .placeName(stamp.getEventPlace().getPlace().getPlaceName())
                .eventCode(stamp.getEventPlace().getEventCode().name())
                .stamped(true)
                .build();
    }
}
