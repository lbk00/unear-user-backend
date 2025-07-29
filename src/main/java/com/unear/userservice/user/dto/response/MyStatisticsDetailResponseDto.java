package com.unear.userservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MyStatisticsDetailResponseDto {

    private Integer totalSpent;

    private Integer totalDiscount;

    private Map<String, Integer> discountByCategory;

    private Map<String, Double> discountCategoryRatio;

    private double spentChangeRatio;

    private double discountChangeRatio;
}
