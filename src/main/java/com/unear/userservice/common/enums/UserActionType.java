package com.unear.userservice.common.enums;

import lombok.Getter;

@Getter
public enum UserActionType {

    BENEFIT_KEYWORD("키워드 검색"),
    BENEFIT_CATEGORY("카테고리 클릭"),
    BENEFIT_DETAIL("혜택 상세 진입"),
    PLACE_FILTER("필터 적용"),
    PLACE_KEYWORD("장소 키워드 검색"),
    VIEW_PLACE_DETAIL("장소 상세 진입"),
    FAVORITE_ON("즐겨찾기 등록"),
    FAVORITE_OFF("즐겨찾기 해제"),
    DOWNLOAD_COUPON("일반 쿠폰 다운로드"),
    DOWNLOAD_FCFS_COUPON("선착순 쿠폰 다운로드"),
    ROULETTE_SPIN("룰렛 돌리기"),
    STAMP_CHECK_IN("스탬프 출석"),
    BENEFIT_USAGE("혜택 사용");

    private final String description;

    UserActionType(String description) {
        this.description = description;
    }

}
