package com.unear.userservice.recommend.dto.request;

import java.math.BigDecimal;

public record LocationBasedRecommendRequestDto(
        BigDecimal latitude,
        BigDecimal longitude
) {}