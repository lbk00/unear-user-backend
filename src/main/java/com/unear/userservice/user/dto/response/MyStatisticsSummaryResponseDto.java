package com.unear.userservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyStatisticsSummaryResponseDto {
    private Integer thisMonthDiscount;
    private List<MonthlyDiscountDto> recentMonthDiscounts;
}
