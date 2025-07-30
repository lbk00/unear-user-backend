package com.unear.userservice.stamp.dto.response;

import com.unear.userservice.place.entity.EventPlace;
import com.unear.userservice.stamp.entity.Stamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventStampResponseDto {

    private List<PlaceStampStatus> stamps;
    private boolean rouletteAvailable;

    @Getter
    @Builder
    public static class PlaceStampStatus {
        private Long eventPlaceId;
        private String placeName;
        private String eventCode;
        private boolean stamped;
    }

    public static EventStampResponseDto of(List<EventPlace> eventPlaces, List<Stamp> stamps, boolean available) {
        Set<Long> stampedPlaceIds = stamps.stream()
                .map(s -> s.getEventPlace().getEventPlaceId())
                .collect(Collectors.toSet());

        List<PlaceStampStatus> result = eventPlaces.stream()
                .filter(ep -> stampedPlaceIds.contains(ep.getEventPlaceId()))
                .map(ep -> PlaceStampStatus.builder()
                        .eventPlaceId(ep.getEventPlaceId())
                        .placeName(ep.getPlace().getPlaceName())
                        .eventCode(ep.getEventCode().getLabel())
                        .stamped(stampedPlaceIds.contains(ep.getEventPlaceId()))
                        .build())
                .toList();

        return EventStampResponseDto.builder()
                .stamps(result)
                .rouletteAvailable(available)
                .build();
    }
}