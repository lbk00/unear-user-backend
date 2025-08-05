package com.unear.userservice.recommend.service.impl;

import com.unear.userservice.place.dto.response.PlaceResponseDto;
import com.unear.userservice.place.entity.Place;
import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;

import com.unear.userservice.recommend.dto.response.RecommendPlaceResponseDto;
import com.unear.userservice.recommend.repository.NearbyPlaceRepository;
import com.unear.userservice.recommend.service.DistanceBasedRecommendService;
import com.unear.userservice.recommend.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DistanceBasedRecommendServiceImpl implements DistanceBasedRecommendService {

    private final NearbyPlaceRepository nearbyPlaceRepository;

    @Override
    public List<RecommendPlaceResponseDto> recommendByDistance(Long userId, LocationBasedRecommendRequestDto request) {
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
                    return RecommendPlaceResponseDto.from(place, distance); // score 없음
                })
                .toList();
    }
}


