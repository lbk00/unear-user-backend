package com.unear.userservice.story.controller;

import com.unear.userservice.common.annotation.LoginUser;
import com.unear.userservice.common.docs.story.StoryApiDocs;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.dto.response.StoryHistoryResponseDto;
import com.unear.userservice.story.service.StoryService;
import com.unear.userservice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/history")
    @StoryApiDocs.GetHistory
    public ResponseEntity<ApiResponse<List<StoryHistoryResponseDto>>> getHistory(@LoginUser User user) {
        List<StoryHistoryResponseDto> history = storyService.getHistory(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success(history));
    }
}
