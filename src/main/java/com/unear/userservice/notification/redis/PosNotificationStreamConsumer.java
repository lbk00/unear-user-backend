package com.unear.userservice.notification.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unear.userservice.notification.dto.request.PosNotificationEventRequest;
import com.unear.userservice.notification.service.impl.SseNotificationService;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PosNotificationStreamConsumer {

    private final SseNotificationService sseNotificationService;
    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamContainer;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void startListening() {
        streamContainer.receive(
                Consumer.from("notification-consumer-group", "consumer-1"),
                StreamOffset.create("pos-notification-stream", ReadOffset.lastConsumed()),
                this::handleMessage
        );
        streamContainer.start();
    }

    private void handleMessage(MapRecord<String, String, String> record) {
        try {
            PosNotificationEventRequest notification = convertToDto(record.getValue());

            log.info("Notification received for userId: {}, type: {}",
                    notification.getUserId(), notification.getType());

            sseNotificationService.sendNotification(notification);

            redisTemplate.opsForStream().acknowledge("notification-consumer-group", record);

            log.info("Message processed successfully: {}", record.getId());

        } catch (Exception e) {
            log.error("메시지 처리 실패: {}", e.getMessage());
        }
    }

    private PosNotificationEventRequest convertToDto(Map<String, String> map) {
        return objectMapper.convertValue(map, PosNotificationEventRequest.class);
    }
}