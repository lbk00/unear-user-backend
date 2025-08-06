package com.unear.userservice.notification.dto.request;

import com.unear.userservice.common.enums.PosNotificationType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class PosNotificationEventRequest {
    private Long userId;
    private int stampOrder;
    private PosNotificationType type;
    private String message;
    private Long relatedPlaceId;
    private String relatedPlaceName;
    private Long relatedEventId;
    private Long discountAmount;
    private Long finalAmount;
}
