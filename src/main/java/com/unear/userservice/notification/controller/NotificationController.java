package com.unear.userservice.notification.controller;

import com.unear.userservice.common.jwt.JwtTokenProvider;
import com.unear.userservice.notification.service.impl.RedisSseEmitterPool;
import lombok.RequiredArgsConstructor;
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
public class NotificationController {

    private final RedisSseEmitterPool emitterPool;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId , @RequestParam String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        Long tokenUserId = jwtTokenProvider.extractUserId(token);
        if (!userId.equals(tokenUserId)) {
            throw new RuntimeException("User ID mismatch");
        }

        return emitterPool.connect(userId);
    }
}