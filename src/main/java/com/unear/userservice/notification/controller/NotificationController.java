package com.unear.userservice.notification.controller;

import com.unear.userservice.common.jwt.JwtTokenProvider;
import com.unear.userservice.notification.service.impl.RedisSseEmitterPool;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
public class NotificationController {

    private final RedisSseEmitterPool emitterPool;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userId , @RequestParam String token, HttpServletResponse response) {
        log.info("🔥 SSE 연결 요청 시작 - userId: {}", userId);

        response.setHeader("Access-Control-Allow-Origin", "https://www.unear.site");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With, Cache-Control");

        return emitterPool.connect(userId);
    }
}