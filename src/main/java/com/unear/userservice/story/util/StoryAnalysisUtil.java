package com.unear.userservice.story.util;

import com.unear.userservice.story.vo.UserAnalysisResult;
import org.springframework.stereotype.Component;

@Component
public class StoryAnalysisUtil {

    public UserAnalysisResult analyze(Long userId) {
        // TODO: 나중에 소비내역 조회 및 정교한 분석 로직 구현

        // 지금은 임시 하드코딩된 분석 결과 반환
        return UserAnalysisResult.builder()
                .foodRate(72.3)
                .avgAmount(14300)
                .activitySummary("배달, 간편식, 길거리 음식 중심 소비")
                .build();
    }
}
