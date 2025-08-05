package com.unear.userservice.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unear.userservice.notification.dto.request.PosNotificationEventRequest;
import com.unear.userservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSubscriber implements MessageListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            PosNotificationEventRequest request = objectMapper.readValue(messageBody, PosNotificationEventRequest.class);

            log.info("Received notification: type={}, userId={}", request.getType(), request.getUserId());
            notificationService.sendNotification(request);

        } catch (Exception e) {
            log.error("Failed to process notification message", e);
        }
    }
}
