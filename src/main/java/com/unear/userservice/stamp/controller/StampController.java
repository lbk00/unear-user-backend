package com.unear.userservice.stamp.controller;

import com.unear.userservice.common.docs.stamp.StampApiDocs;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.common.security.CustomUser;
import com.unear.userservice.stamp.dto.response.EventStampResponseDto;
import com.unear.userservice.stamp.service.StampService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stamps")
@RequiredArgsConstructor
public class StampController {

    private final StampService stampService;

    @StampApiDocs.GetMyStampStatus
    @GetMapping("/events/{eventId}/me")
    public ResponseEntity<ApiResponse<EventStampResponseDto>> getMyStampsForEvent(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long eventId
    ) {
        EventStampResponseDto response = stampService.getMyStampsForEvent(user.getId(), eventId);
        return ResponseEntity.ok(ApiResponse.success("스탬프 조회 성공", response));
    }
}