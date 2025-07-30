package com.unear.userservice.notification.controller;

import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.notification.dto.request.PosEventRequest;
import com.unear.userservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/pos")
@RequiredArgsConstructor
public class PosNotificationController {

    private final NotificationService notificationService;


    @PostMapping("/notify")
    public ApiResponse<String> notify(@RequestBody PosEventRequest request) {
        notificationService.sendNotification(request);
        return ApiResponse.success("포스 알림 전송 완료");
    }
}
