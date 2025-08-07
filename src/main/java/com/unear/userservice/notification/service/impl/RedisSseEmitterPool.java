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
        SseEmitter emitter = new SseEmitter(0L);
        localEmitters.put(userId, emitter);

        stringRedisTemplate.opsForValue().set(
                "user_connection:" + userId,
                instanceId,
                Duration.ofMinutes(2)
        );

        log.info("[SSE-CONNECT] userId={} connected to instanceId={}", userId, instanceId);

        emitter.onCompletion(() -> {
            log.info("[SSE-COMPLETE] userId={} completed connection", userId);
            cleanup(userId);
        });

        emitter.onTimeout(() -> {
            log.warn("[SSE-TIMEOUT] userId={} SSE timeout", userId);
            cleanup(userId);
        });

        emitter.onError(e -> {
            log.error("[SSE-ERROR] userId={} error occurred: {}", userId, e.toString());
            cleanup(userId);
        });

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
            log.info("[SSE-SEND] Initial dummy event sent to userId={}", userId);
        } catch (IOException e) {
            log.error("[SSE-ERROR] Failed to send initial event to userId={}: {}", userId, e.toString());
            emitter.completeWithError(e);
        }
        return emitter;
    }

    public void sendToUser(Long userId, String eventName, Object data) {
        String targetInstance = stringRedisTemplate.opsForValue().get("user_connection:" + userId);
        log.debug("[SSE-SEND-TRY] Try send to userId={}, targetInstance={}, currentInstance={}", userId, targetInstance, instanceId);

        if (instanceId.equals(targetInstance)) {
            SseEmitter emitter = localEmitters.get(userId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(data));
                    log.info("[SSE-SEND] userId={} eventName={} sent successfully", userId, eventName);
                } catch (IOException e) {
                    log.error("[SSE-SEND-FAIL] Failed to send event to userId={}, eventName={}, err={}", userId, eventName, e.toString());
                    cleanup(userId);
                }
            } else {
                log.warn("[SSE-SEND-WARN] No emitter found for userId={}", userId);
            }
        } else {
            log.debug("[SSE-SKIP] userId={} not connected to this instance", userId);
        }
    }

    public boolean isConnected(Long userId) {
        String connectedInstance = stringRedisTemplate.opsForValue().get("user_connection:" + userId);
        boolean result = localEmitters.containsKey(userId) && instanceId.equals(connectedInstance);
        log.debug("[SSE-CHECK] userId={} connectedInstance={}, currentInstance={}, result={}",
                userId, connectedInstance, instanceId, result);
        return result;
    }

    public SseEmitter get(Long userId) {
        SseEmitter emitter = localEmitters.get(userId);
        log.debug("[SSE-GET] userId={} emitter={}", userId, emitter != null ? "exists" : "null");
        return emitter;
    }

    public void remove(Long userId) {
        log.info("[SSE-REMOVE] userId={} remove requested", userId);
        cleanup(userId);
    }

    private void cleanup(Long userId) {
        localEmitters.remove(userId);
        stringRedisTemplate.delete("user_connection:" + userId);
        log.info("[SSE-CLEANUP] userId={} disconnected and cleaned up from instanceId={}", userId, instanceId);
    }

    public void sendPingToAll() {
        for (Map.Entry<Long, SseEmitter> entry : localEmitters.entrySet()) {
            Long userId = entry.getKey();
            SseEmitter emitter = entry.getValue();
            try {
                emitter.send(SseEmitter.event()
                        .name("ping")
                        .data("keep-alive"));  // 아무 데이터나 가능
                log.debug("[SSE-PING] userId={} keep-alive 전송", userId);
            } catch (Exception e) {
                log.warn("[SSE-PING] userId={} ping 실패: {}", userId, e.toString());
                emitter.completeWithError(e);
            }
        }
    }
}