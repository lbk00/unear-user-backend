package com.unear.userservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MyStatisticsDetailResponseDto {

    private Integer totalSpent;

    private Integer totalDiscount;

    private Map<String, Integer> discountByCategory = new HashMap<>();

    private Map<String, Double> discountCategoryRatio = new HashMap<>();

    private double spentChangeRatio;

    private double discountChangeRatio;
}
