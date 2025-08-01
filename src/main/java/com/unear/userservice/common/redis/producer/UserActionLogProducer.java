package com.unear.userservice.common.redis.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unear.userservice.common.enums.UserActionType;
import com.unear.userservice.common.util.LogMetadataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionLogProducer {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String STREAM_KEY = "stream:user_action_logs";

    @Async
    public void sendLog(String userId, String actionType, String screen ,String metadata) {
        try {
            String dedupKey = String.format("logdedup:%s:%s:%s:%s", userId, actionType, screen, Math.abs(metadata.hashCode()) % 1000000);

            Boolean isNew = redisTemplate.opsForValue().setIfAbsent(dedupKey, "1", Duration.ofSeconds(2));
            if (Boolean.FALSE.equals(isNew)) {
                return;
            }

            Map<String, String> data = new HashMap<>();
            data.put("userId", userId);
            data.put("actionType", actionType);
            data.put("screen", screen);
            data.put("metadata", metadata);
            data.put("timestamp", String.valueOf(System.currentTimeMillis()));
            log.info("[로그 전송] userId={}, actionType={}, screen={}, metadata={}", userId, actionType, screen, metadata);

            redisTemplate.opsForStream()
                    .add(MapRecord.create(STREAM_KEY, data));
        } catch (Exception e) {
            log.error("Redis 스트림 로그 전송 실패: userId={}, actionType={}", userId, actionType, e);
            }
    }

    public void logUserAction(Long userId, UserActionType actionType, String screen, Map<String, Object> metadataMap) {
        if (userId == null) return;
        try {
            Map<String, Object> cleanMetadata = LogMetadataUtils.sanitizeMapValues(metadataMap);
            String metadataJson = objectMapper.writeValueAsString(cleanMetadata);
            sendLog(userId.toString(), actionType.name(), screen, metadataJson);
        } catch (Exception e) {
            log.warn("{} 로그 전송 실패", actionType.name(), e);
        }
    }


}
