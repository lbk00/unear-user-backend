package com.unear.userservice.stamp.service;

import com.unear.userservice.stamp.dto.response.EventStampResponseDto;

public interface StampService {
    EventStampResponseDto getMyStampsForEvent(Long userId, Long eventId);
}