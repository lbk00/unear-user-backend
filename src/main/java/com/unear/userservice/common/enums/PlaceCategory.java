package com.unear.userservice.common.enums;

public enum PlaceCategory {
    FOOD("FOOD", "푸드"),
    LIFE("LIFE", "생활/편의"),
    BEAUTY("BEAUTY", "뷰티/건강"),
    ACTIVITY("ACTIVITY", "액티비티"),
    EDUCATION("EDUCATION","교육"),
    CULTURE("CULTURE", "문화/여가"),
    BAKERY("BAKERY", "베이커리"),
    SHOPPING("SHOPPING", "쇼핑"),
    CAFE("CAFE", "카페");

    private final String code;
    private final String description;

    PlaceCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public static PlaceCategory fromCode(String code) {
        for (PlaceCategory value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid CategoryCode: " + code);
    }
}
