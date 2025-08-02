package com.unear.userservice.common.enums;

import lombok.Getter;

@Getter
public enum UserActionType {

    BENEFIT_KEYWORD("키워드 검색"),
    BENEFIT_DETAIL("혜택 페이지 진입"),
    PLACE_FILTER("필터 적용"),
    PLACE_KEYWORD("장소 키워드 검색"),
    VIEW_PLACE_DETAIL("장소 상세 진입"),
    FAVORITE_ON("즐겨찾기 등록"),
    FAVORITE_OFF("즐겨찾기 해제"),
    DOWNLOAD_COUPON("일반 쿠폰 다운로드"),
    DOWNLOAD_FCFS_COUPON("선착순 쿠폰 다운로드"),
    POS_USAGE("결제시 사용된 혜택");

    private final String description;

    UserActionType(String description) {
        this.description = description;
    }

}
