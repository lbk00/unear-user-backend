package com.unear.userservice.notification.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unear.userservice.common.enums.PosNotificationType;
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
        return PosNotificationEventRequest.builder()
                .userId(parseLong(map.get("userId")))
                .stampOrder(parseInt(map.get("stampOrder"), 0))
                .type(parseEnum(map.get("type")))
                .message(map.getOrDefault("message", ""))
                .relatedPlaceId(parseLong(map.get("relatedPlaceId")))
                .relatedPlaceName(map.get("relatedPlaceName"))
                .relatedEventId(parseLong(map.get("relatedEventId")))
                .discountAmount(parseLong(map.get("discountAmount")))
                .finalAmount(parseLong(map.get("finalAmount")))
                .build();
    }

    private int parseInt(String val, int defaultVal) {
        if (val == null || val.isBlank()) return defaultVal;
        val = val.replaceAll("\"", "").trim();
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            log.warn("잘못된 Int 형식: {}", val);
            return defaultVal;
        }
    }

    private Long parseLong(String val) {
        if (val == null) return null;
        val = val.replaceAll("\"", "").trim();
        try { return Long.parseLong(val); } catch (NumberFormatException e) {
            log.warn("잘못된 Long 형식: {}", val);
            return null;
        }
    }

    private PosNotificationType parseEnum(String val) {
        if (val == null || val.isBlank()) return null;
        val = val.replaceAll("\"", "").trim();
        try {
            return PosNotificationType.valueOf(val);
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 Enum 값: {}", val);
            return null;
        }
    }


}