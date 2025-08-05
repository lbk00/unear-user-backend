package com.unear.userservice.story.controller;

import com.unear.userservice.common.annotation.LoginUser;
import com.unear.userservice.common.docs.story.StoryApiDocs;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.common.security.CustomUser;
import com.unear.userservice.common.security.CustomUserDetailsService;
import com.unear.userservice.story.dto.request.StoryCreateRequestDto;
import com.unear.userservice.story.dto.response.StoryCurrentResponseDto;
import com.unear.userservice.story.dto.response.StoryDetailResponseDto;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.service.StoryService;
import com.unear.userservice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @GetMapping("/diagnosis")
    @StoryApiDocs.GetDiagnosis
    public ResponseEntity<ApiResponse<StoryDiagnosisResponseDto>> getDiagnosis(@LoginUser User user) {
        StoryDiagnosisResponseDto response = storyService.getDiagnosis(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping()
    @StoryApiDocs.GetStory
    public ResponseEntity<List<StoryDetailResponseDto>> getUserStory(
            @AuthenticationPrincipal CustomUser userDetails,
            @RequestParam("targetMonth") String targetMonth
    ) {
        Long userId = userDetails.getId(); // 여기서 추출
        List<StoryDetailResponseDto> stories = storyService.getUserStories(userId, targetMonth);
        return ResponseEntity.ok(stories);
    }



    @PostMapping
    @StoryApiDocs.CreateStory
    public ResponseEntity<ApiResponse<Void>> createStory(@LoginUser User user) {
        StoryCreateRequestDto request = StoryCreateRequestDto.builder()
                .targetMonth(YearMonth.now()) // 필수 필드만 채워서 넘김
                .build();
        storyService.createStory(user.getUserId(), request );
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
