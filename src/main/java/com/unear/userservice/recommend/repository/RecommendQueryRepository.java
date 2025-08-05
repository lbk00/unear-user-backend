package com.unear.userservice.recommend.repository;

import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.PlaceResponseDto;

import java.util.List;

public interface RecommendQueryRepository {
    List<PlaceResponseDto> recommendPlaces(Long userId, LocationBasedRecommendRequestDto request);
}
