package com.unear.userservice.recommend.controller;

import com.unear.userservice.common.annotation.LoginUser;
import com.unear.userservice.common.docs.recommend.RecommendApiDocs;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;

import com.unear.userservice.recommend.dto.response.RecommendPlaceResponseDto;
import com.unear.userservice.recommend.service.VectorBasedRecommendService;
import com.unear.userservice.recommend.service.DistanceBasedRecommendService;

import com.unear.userservice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final DistanceBasedRecommendService distanceBasedRecommendService;
    private final VectorBasedRecommendService vectorBasedRecommendService;

    @RecommendApiDocs.GetRecommendPlaces
    @PostMapping("/places")
    public ResponseEntity<ApiResponse<List<RecommendPlaceResponseDto>>> recommendPlaces(
            @LoginUser User user,
            @RequestBody LocationBasedRecommendRequestDto request
    ) {
        List<RecommendPlaceResponseDto> results = vectorBasedRecommendService.recommendWithScore(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}
