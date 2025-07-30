package com.unear.userservice.notification.service.impl;

import com.unear.userservice.notification.dto.request.PosEventRequest;
import com.unear.userservice.notification.dto.response.PaymentSuccessResponse;
import com.unear.userservice.notification.dto.response.StampAddedResponse;
import com.unear.userservice.notification.dto.response.StampCompletedResponse;
import com.unear.userservice.notification.service.NotificationService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseNotificationService implements NotificationService {

    private final SseEmitterPool emitterPool;

    @Override
    public void sendNotification(PosEventRequest request) {
        Long userId = request.getUserId();
        Object responseDto;
        String eventName;

        try {
            switch (request.getType()) {
                case STAMP_ADDED -> {
                    eventName = "stamp-added";
                    responseDto = StampAddedResponse.from(request);
                }
                case STAMP_COMPLETED -> {
                    eventName = "stamp-completed";
                    responseDto = StampCompletedResponse.from(request);
                }
                case PAYMENT_SUCCESS -> {
                    eventName = "payment-success";
                    responseDto = PaymentSuccessResponse.from(request);
                }
                default -> {
                    return;
                }
            }

            emitterPool.sendToUser(userId, eventName, responseDto);

        } catch (Exception e) {
            emitterPool.remove(userId);
        }
    }
}
