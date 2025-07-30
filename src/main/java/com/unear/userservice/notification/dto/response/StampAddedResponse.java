package com.unear.userservice.notification.dto.response;

import com.unear.userservice.common.enums.PosNotificationType;
import com.unear.userservice.notification.dto.request.PosNotificationEventRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StampAddedResponse {
    private PosNotificationType type;
    private String message;
    private int stampOrder;
    private Long relatedPlaceId;
    private String relatedPlaceName;

    public static StampAddedResponse from(PosNotificationEventRequest request) {
        return StampAddedResponse.builder()
                .type(request.getType())
                .message(request.getMessage())
                .relatedPlaceId(request.getRelatedPlaceId())
                .relatedPlaceName(request.getRelatedPlaceName())
                .stampOrder(request.getStampOrder())
                .build();
    }
}
