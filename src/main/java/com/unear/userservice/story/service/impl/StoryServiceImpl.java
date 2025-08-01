package com.unear.userservice.story.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unear.userservice.story.dto.response.StoryCurrentResponseDto;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.dto.response.StoryHistoryResponseDto;
import com.unear.userservice.story.service.StoryService;
import com.unear.userservice.story.util.StoryAnalysisUtil;
import com.unear.userservice.story.util.StoryAiClient;
import com.unear.userservice.story.vo.UserAnalysisResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryAnalysisUtil analysisUtil;
    private final StoryAiClient aiClient;
    private final ObjectMapper objectMapper;

    @Override
    public StoryDiagnosisResponseDto getDiagnosis(Long userId) {
        // 1. 사용자 소비 내역 분석
        UserAnalysisResult analysisResult = analysisUtil.analyze(userId);

        // 2. 프롬프트 생성 후 AI 호출
        String prompt = aiClient.generatePrompt(analysisResult);
        String aiResponse = aiClient.requestDiagnosis(prompt);

        // 3. AI 응답 파싱
        try {
            JsonNode node = objectMapper.readTree(aiResponse);
            return StoryDiagnosisResponseDto.builder()
                    .type(node.get("type").asText())
                    .comment(node.get("comment").asText())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("AI 응답 파싱 실패", e);
        }
    }

    @Override
    public List<StoryHistoryResponseDto> getHistory(Long userId) {
        // TODO: 임시 데이터, 추후 DB 또는 AI 진단 기반으로 대체
        return List.of(
                StoryHistoryResponseDto.builder()
                        .month(YearMonth.of(2025, 8))
                        .type("쩝쩝박사")
                        .comment("축제의 즐거움은 맛에 있다고 믿는 당신!")
                        .imageUrl("https://cdn.unear.site/story/type/zzep.png")
                        .build(),

                StoryHistoryResponseDto.builder()
                        .month(YearMonth.of(2025, 7))
                        .type("알뜰소비러")
                        .comment("혜택은 놓치지 않는다! 알뜰하고 계획적인 당신!")
                        .imageUrl("https://cdn.unear.site/story/type/saver.png")
                        .build()
        );
    }

    @Override
    public StoryCurrentResponseDto getCurrent(Long userId) {
        // 대표 결제 내역
        StoryCurrentResponseDto.RepresentativeLog repLog =
                StoryCurrentResponseDto.RepresentativeLog.builder()
                        .date(LocalDate.of(2025, 7, 15))
                        .storeName("맥도날드 치즈버거")
                        .amount(14200)
                        .logoUrl("https://cdn.unear.site/logo/mcdonalds.png")
                        .build();

        return StoryCurrentResponseDto.builder()
                .month(YearMonth.of(2025, 7))
                .comment("맛있는 음식 도착 소식에, 행복해지는 순간이 찾아왔어.")
                .imageUrl("https://cdn.unear.site/story/current/zzep_full.png")
                .representativeLog(repLog)
                .build();
    }
}
