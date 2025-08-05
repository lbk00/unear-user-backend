package com.unear.userservice.notification.service.impl;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSseEmitterPool {
    private final StringRedisTemplate stringRedisTemplate;
    private final Map<Long, SseEmitter> localEmitters = new ConcurrentHashMap<>();
    private final String instanceId = UUID.randomUUID().toString();



    public SseEmitter connect(Long userId) {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);

        localEmitters.put(userId, emitter);

        stringRedisTemplate.opsForValue().set(
                "user_connection:" + userId,
                instanceId,
                Duration.ofMinutes(2)
        );

        log.info("User {} connected to instance {}", userId, instanceId);

        emitter.onCompletion(() -> cleanup(userId));
        emitter.onTimeout(() -> cleanup(userId));
        emitter.onError(e -> cleanup(userId));

        return emitter;
    }

    public void sendToUser(Long userId, String eventName, Object data) {
        String targetInstance = stringRedisTemplate.opsForValue().get("user_connection:" + userId);

        if (instanceId.equals(targetInstance)) {
            SseEmitter emitter = localEmitters.get(userId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(data));
                    log.info("Notification sent to user {} on instance {}", userId, instanceId);
                } catch (IOException e) {
                    log.error("Failed to send notification to user {}", userId, e);
                    cleanup(userId);
                }
            }
        } else {
            log.debug("User {} not connected to this instance. Target: {}, Current: {}",
                    userId, targetInstance, instanceId);
        }
    }

    public boolean isConnected(Long userId) {
        return localEmitters.containsKey(userId) &&
                instanceId.equals(stringRedisTemplate.opsForValue().get("user_connection:" + userId));
    }

    public SseEmitter get(Long userId) {
        return localEmitters.get(userId);
    }

    public void remove(Long userId) {
        cleanup(userId);
    }

    private void cleanup(Long userId) {
        localEmitters.remove(userId);
        stringRedisTemplate.delete("user_connection:" + userId);
        log.info("User {} disconnected from instance {}", userId, instanceId);
    }
}
