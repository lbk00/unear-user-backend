package com.unear.userservice.recommend.service;

import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.PlaceResponseDto;
import com.unear.userservice.recommend.repository.PlaceRepository;
import com.unear.userservice.place.entity.Place;
import com.unear.userservice.recommend.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final PlaceRepository placeRepository;

    public List<PlaceResponseDto> recommendPlaces(LocationBasedRecommendRequestDto request) {
        double lat = request.latitude().doubleValue();
        double lng = request.longitude().doubleValue();
        double radius = 300.0;

        List<Place> places = placeRepository.findNearbyPlaces(lat, lng, radius);

        return places.stream()
                .map(place -> {
                    double distance = GeoUtils.calculateDistance(
                            lat, lng,
                            place.getLatitude().doubleValue(), place.getLongitude().doubleValue()
                    );
                    return PlaceResponseDto.from(place, distance);
                })
                .toList();
    }
}
