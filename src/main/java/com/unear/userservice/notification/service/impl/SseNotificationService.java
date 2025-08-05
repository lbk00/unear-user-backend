package com.unear.userservice.notification.service.impl;

import com.unear.userservice.notification.dto.request.PosNotificationEventRequest;
import com.unear.userservice.notification.dto.response.PaymentSuccessResponse;
import com.unear.userservice.notification.dto.response.StampAddedResponse;
import com.unear.userservice.notification.dto.response.StampCompletedResponse;
import com.unear.userservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SseNotificationService implements NotificationService {

    private final RedisSseEmitterPool emitterPool;

    @Override
    public void sendNotification(PosNotificationEventRequest request) {
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
