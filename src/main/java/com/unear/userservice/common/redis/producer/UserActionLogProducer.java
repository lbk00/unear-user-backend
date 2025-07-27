package com.unear.userservice.common.redis.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserActionLogProducer {

    private final RedisTemplate<String, String> redisTemplate;

    // RedisStream 키
    private static final String STREAM_KEY = "stream:user_action_logs";

    // 사용자 행동로그를 RedisStream에 기록
    public void sendLog(String userId, String actionType, String metadata) {
        Map<String, String> data = new HashMap<>();
        data.put("userId", userId); // 사용자 id
        data.put("actionType", actionType); // 행동 유형
        data.put("metadata", metadata); // 부가 정보
        data.put("timestamp", String.valueOf(System.currentTimeMillis())); // 시간

        // XADD
        redisTemplate.opsForStream()
                .add(MapRecord.create(STREAM_KEY, data));
    }
}
