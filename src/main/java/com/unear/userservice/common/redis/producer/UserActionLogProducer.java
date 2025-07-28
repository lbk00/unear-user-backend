package com.unear.userservice.common.redis.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionLogProducer {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String STREAM_KEY = "stream:user_action_logs";

    @Async
    public void sendLog(String userId, String actionType, String screen , String metadata) {
        Map<String, String> data = new HashMap<>();
        data.put("userId", userId);
        data.put("actionType", actionType);
        data.put("screen", screen);
        data.put("metadata", metadata);
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        log.info("[로그 전송] userId={}, actionType={}, screen={}, metadata={}", userId, actionType, screen, metadata);

        redisTemplate.opsForStream()
                .add(MapRecord.create(STREAM_KEY, data));
    }
}
