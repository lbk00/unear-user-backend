package com.unear.userservice.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long userCouponId;

    private Long placeId;

    private LocalDateTime usedAt;

    private Integer originalAmount;

    private Integer membershipDiscountAmount;

    private Integer couponDiscountAmount;

    private Integer totalDiscountAmount;

    private Integer totalPaymentAmount;

    private Boolean isCouponUsed;

    private Boolean isMembershipUsed;

    private LocalDateTime paidAt;

    private String discountCode;

    private String membershipCode;

    private String placeCategory;

}

