package com.unear.userservice.recommend.dto.response;

import com.unear.userservice.place.dto.response.PlaceResponseDto;
import com.unear.userservice.place.entity.Place;

import java.math.BigDecimal;

public record RecommendPlaceResponseDto(
        Long placeId,
        String placeName,
        BigDecimal latitude,
        BigDecimal longitude,
        double distanceInMeters,
        BigDecimal score
) {
    public static RecommendPlaceResponseDto from(Place place, double distance, BigDecimal score) {
        return new RecommendPlaceResponseDto(
                place.getPlaceId(),
                place.getPlaceName(),
                place.getLatitude(),
                place.getLongitude(),
                distance,
                score
        );
    }

    public static RecommendPlaceResponseDto from(Place place, double distance) { //DistanceBasedRecommandService 에서 쓰임
        return new RecommendPlaceResponseDto(
                place.getPlaceId(),
                place.getPlaceName(),
                place.getLatitude(),
                place.getLongitude(),
                distance,
                null // 점수는 없는 버전
        );
    }
}
