package com.unear.userservice.notification.service;

import com.unear.userservice.notification.dto.request.PosNotificationEventRequest;

public interface NotificationService {
    void sendNotification(PosNotificationEventRequest request);
}
