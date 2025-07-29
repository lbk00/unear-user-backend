package com.unear.userservice.user.dto.response;

import com.unear.userservice.user.entity.UserHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserHistoryResponseDto {

    private String placeName;

    private LocalDateTime usedAt;

    private Integer originalAmount;

    private Integer totalDiscountAmount;

    private Integer totalPaymentAmount;

    private Boolean isCouponUsed;

    private Boolean isMembershipUsed;

    private String discountCode;

    private String membershipCode;

    private String placeCategory;

    public static UserHistoryResponseDto from(UserHistory entity) {
        return UserHistoryResponseDto.builder()
                .usedAt(entity.getUsedAt())
                .originalAmount(entity.getOriginalAmount())
                .totalDiscountAmount(entity.getTotalDiscountAmount())
                .totalPaymentAmount(entity.getTotalPaymentAmount())
                .isCouponUsed(entity.getIsCouponUsed())
                .isMembershipUsed(entity.getIsMembershipUsed())
                .discountCode(entity.getDiscountCode())
                .membershipCode(entity.getMembershipCode())
                .placeCategory(entity.getPlaceCategory())
                .build();
    }
}

