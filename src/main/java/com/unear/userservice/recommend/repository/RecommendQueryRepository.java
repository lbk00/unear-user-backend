package com.unear.userservice.recommend.repository;

import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.RecommendPlaceResponseDto;

import java.util.List;

public interface RecommendQueryRepository {
    List<RecommendPlaceResponseDto> recommendPlaces(Long userId, LocationBasedRecommendRequestDto request);
}
