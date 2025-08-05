package com.unear.userservice.recommend.service.impl;

import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.RecommendPlaceResponseDto;
import com.unear.userservice.recommend.repository.RecommendQueryRepository;
import com.unear.userservice.recommend.service.VectorBasedRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorBasedRecommendServiceImpl implements VectorBasedRecommendService {

    private final RecommendQueryRepository recommendQueryRepository;

    @Override
    public List<RecommendPlaceResponseDto> recommendWithScore(Long userId, LocationBasedRecommendRequestDto request) {
        return recommendQueryRepository.recommendPlaces(userId, request); // 이 안에서 nativeQuery 실행
    }
}
