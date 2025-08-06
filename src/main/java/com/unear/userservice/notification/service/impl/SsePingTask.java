package com.unear.userservice.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsePingTask {

    private final RedisSseEmitterPool emitterPool;

    @Scheduled(fixedRate = 30000) // 30초마다 실행
    public void sendPing() {
        emitterPool.sendPingToAll();
    }
}