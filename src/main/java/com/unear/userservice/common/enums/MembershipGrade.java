package com.unear.userservice.common.enums;

import com.unear.userservice.common.exception.exception.InvalidCodeException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MembershipGrade {

    VVIP("VVIP", "VVIP", 1),
    VIP("VIP", "VIP", 2),
    BASIC("BASIC", "우수", 3),
    ALL("ALL", "모든등급", 4);

    private final String code;
    private final String label;
    private final int priority;

    MembershipGrade(String code, String label, int priority) {
        this.code = code;
        this.label = label;
        this.priority = priority;
    }

    public static MembershipGrade fromCode(String code) {
        return Arrays.stream(values())
                .filter(g -> g.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new InvalidCodeException("Invalid membership code: " + code));
    }

    public static boolean isAll(String code) {
        return ALL.code.equalsIgnoreCase(code);
    }
}
