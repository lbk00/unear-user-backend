package com.unear.userservice.benefit.dto.response;

import com.unear.userservice.common.enums.MembershipGrade;
import com.unear.userservice.place.entity.Franchise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseDiscountPolicyDetailResponseDto {

    private Long franchiseId;
    private String franchiseName;
    private String imageUrl;
    private String categoryCode;

    private List<MembershipGradePolicyResponseDto> membershipPolicies;

    public static FranchiseDiscountPolicyDetailResponseDto from(Franchise franchise) {
        List<MembershipGradePolicyResponseDto> policies = franchise.getFranchiseDiscountPolicies().stream()
                .map(MembershipGradePolicyResponseDto::from)
                .sorted(Comparator.comparingInt(p ->
                        MembershipGrade.fromCode(p.getMembershipCode()).getPriority()))
                .toList();

        return new FranchiseDiscountPolicyDetailResponseDto(
                franchise.getFranchiseId(),
                franchise.getName(),
                franchise.getImageUrl(),
                franchise.getCategoryCode(),
                policies
        );
    }
}



