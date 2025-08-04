package com.unear.userservice.recommend.dto.response;

import com.unear.userservice.place.entity.Place;
import java.math.BigDecimal;

public record PlaceResponseDto(
        Long placeId,
        String placeName,
        BigDecimal latitude,
        BigDecimal longitude,
        double distanceInMeters
) {
    public static PlaceResponseDto from(Place place, double distance) {
        return new PlaceResponseDto(
                place.getPlaceId(),
                place.getPlaceName(),
                place.getLatitude(),
                place.getLongitude(),
                distance
        );
    }
}
