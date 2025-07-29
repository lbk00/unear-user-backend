package com.unear.userservice.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyDiscountDto {
    private String month;
    private Integer discount;
}
