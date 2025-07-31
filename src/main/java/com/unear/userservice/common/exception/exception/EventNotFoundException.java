package com.unear.userservice.common.exception.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long eventId) {
        super("이벤트를 찾을 수 없습니다: id=" + eventId);
    }
}
