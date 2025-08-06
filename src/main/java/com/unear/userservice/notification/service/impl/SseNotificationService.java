package com.unear.userservice.notification.service.impl;

import com.unear.userservice.notification.dto.request.PosNotificationEventRequest;
import com.unear.userservice.notification.dto.response.PaymentSuccessResponse;
import com.unear.userservice.notification.dto.response.StampAddedResponse;
import com.unear.userservice.notification.dto.response.StampCompletedResponse;
import com.unear.userservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseNotificationService implements NotificationService {

    private final RedisSseEmitterPool emitterPool;

    @Override
    public void sendNotification(PosNotificationEventRequest request) {
        Long userId = request.getUserId();
        Object responseDto;
        String eventName;

        log.info("[NOTIFY] Received request: userId={}, type={}", userId, request.getType());

        try {
            switch (request.getType()) {
                case STAMP_ADDED -> {
                    eventName = "stamp-added";
                    responseDto = StampAddedResponse.from(request);
                    log.debug("[NOTIFY] Type=STAMP_ADDED, eventName={}, response={}", eventName, responseDto);
                }
                case STAMP_COMPLETED -> {
                    eventName = "stamp-completed";
                    responseDto = StampCompletedResponse.from(request);
                    log.debug("[NOTIFY] Type=STAMP_COMPLETED, eventName={}, response={}", eventName, responseDto);
                }
                case PAYMENT_SUCCESS -> {
                    eventName = "payment-success";
                    responseDto = PaymentSuccessResponse.from(request);
                    log.debug("[NOTIFY] Type=PAYMENT_SUCCESS, eventName={}, response={}", eventName, responseDto);
                }
                default -> {
                    log.warn("[NOTIFY] Unknown notification type received: {}", request.getType());
                    return;
                }
            }

            emitterPool.sendToUser(userId, eventName, responseDto);
            log.info("[NOTIFY-SEND!!] Sent event '{}' to userId={}", eventName, userId);

        } catch (Exception e) {
            log.error("[NOTIFY-FAIL] Failed to send notification to userId={}, err={}", userId, e.toString());
            emitterPool.remove(userId);
        }
    }
}