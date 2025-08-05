package com.unear.userservice.recommend.service;


import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.PlaceResponseDto;

import java.util.List;

public interface DistanceBasedRecommendService {
    List<PlaceResponseDto> recommendByDistance(Long userId, LocationBasedRecommendRequestDto request);
}
