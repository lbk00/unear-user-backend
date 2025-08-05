package com.unear.userservice.notification.controller;

import com.unear.userservice.common.jwt.JwtTokenProvider;
import com.unear.userservice.notification.service.impl.RedisSseEmitterPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4000", "https://dev.unear.site"}, allowCredentials = "true")
public class NotificationController {

    private final RedisSseEmitterPool emitterPool;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId , @RequestParam String token) {
        log.info("🔥 SSE 연결 요청 시작 - userId: {}", userId);
        return emitterPool.connect(userId);
    }
}