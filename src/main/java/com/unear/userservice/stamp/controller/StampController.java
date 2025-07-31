package com.unear.userservice.stamp.controller;

import com.unear.userservice.common.docs.stamp.StampApiDocs;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.common.security.CustomUser;
import com.unear.userservice.stamp.dto.response.StampStatusResponseDto;
import com.unear.userservice.stamp.service.StampService;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("events/{eventId}/me")
    public ApiResponse<StampStatusResponseDto> getMyStamps(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long eventId
    ) {
        return ApiResponse.success(stampService.getMyStampsForEvent(user.getId(), eventId));
    }
}