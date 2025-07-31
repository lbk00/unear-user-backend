package com.unear.userservice.stamp.service;

import com.unear.userservice.stamp.dto.response.StampStatusResponseDto;

public interface StampService {
    StampStatusResponseDto getMyStampsForEvent(Long userId, Long eventId);
}
