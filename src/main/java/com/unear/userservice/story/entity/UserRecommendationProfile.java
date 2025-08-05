package com.unear.userservice.story.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_recommendation_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserRecommendationProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "payment_type_tag")
    private String paymentTypeTag;

    @Column(name = "hashtag")
    private String hashtag;

    // 필요하면 createdAt, updatedAt 등 추가
}
