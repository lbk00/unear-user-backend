package com.unear.userservice.recommend.service;

import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.RecommendPlaceResponseDto;

import java.util.List;

public interface VectorBasedRecommendService {
    List<RecommendPlaceResponseDto> recommendWithScore(Long userId, LocationBasedRecommendRequestDto request);
}
