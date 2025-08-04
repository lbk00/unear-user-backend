package com.unear.userservice.recommend.controller;

import com.unear.userservice.common.annotation.LoginUser;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;
import com.unear.userservice.recommend.dto.response.PlaceResponseDto;
import com.unear.userservice.recommend.service.RecommendService;
import com.unear.userservice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/places")
    public ResponseEntity<ApiResponse<List<PlaceResponseDto>>> recommendPlacesByLocation(
            @LoginUser User user,
            @RequestBody LocationBasedRecommendRequestDto request
    ) {
        List<PlaceResponseDto> results = recommendService.recommendPlaces(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

}
