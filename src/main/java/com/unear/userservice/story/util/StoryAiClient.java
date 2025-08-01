package com.unear.userservice.story.util;

import com.unear.userservice.story.vo.UserAnalysisResult;
import org.springframework.stereotype.Component;

@Component
public class StoryAiClient {

    public String generatePrompt(UserAnalysisResult result) {
        return String.format("""
            사용자의 최근 소비 분석 결과입니다:
            - 음식 비중: %.2f%%
            - 평균 결제 금액: %d원
            - 주요 활동: %s

            위 정보를 기반으로 사용자의 결제 타입(type)과 설명(comment)을 JSON 형태로 생성해줘.
            """, result.foodRate(), result.avgAmount(), result.activitySummary());
    }

    public String requestDiagnosis(String prompt) {
        // TODO: 나중에 실제 OpenAI API 호출로 대체

        // 지금은 하드코딩된 임시 JSON 응답
        return """
            {
              "type": "쩝쩝박사",
              "comment": "축제의 즐거움은 맛에 있다고 믿는 당신! 다양한 먹거리를 탐험하며..."
            }
        """;
    }
}
