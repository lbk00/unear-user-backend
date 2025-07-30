package com.unear.userservice.notification.service;

import com.unear.userservice.notification.dto.request.PosEventRequest;

public interface NotificationService {
    void sendNotification(PosEventRequest request);
}
