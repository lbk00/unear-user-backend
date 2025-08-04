package com.unear.userservice.recommend.service;

import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.PlaceResponseDto;
import com.unear.userservice.recommend.repository.NearbyPlaceRepository;
import com.unear.userservice.place.entity.Place;
import com.unear.userservice.recommend.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final NearbyPlaceRepository nearbyPlaceRepository;

    public List<PlaceResponseDto> recommendPlaces(Long userId, LocationBasedRecommendRequestDto request) {
        double lat = request.latitude().doubleValue();
        double lng = request.longitude().doubleValue();
        double radius = 300.0;

        List<Place> places = nearbyPlaceRepository.findNearbyPlaces(lat, lng, radius);

        return places.stream()
                .map(place -> {
                    double distance = GeoUtils.calculateDistance(
                            lat, lng,
                            place.getLatitude().doubleValue(), place.getLongitude().doubleValue()
                    );
//                    double score = calculateUserScore(userId, place, distance); // <- 개인화 추천 점수 예시
                    return PlaceResponseDto.from(place, distance);
                })
                .toList();
    }
}
