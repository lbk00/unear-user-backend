package com.unear.userservice.story.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.service.StoryService;
import com.unear.userservice.story.util.StoryAnalysisUtil;
import com.unear.userservice.story.util.StoryAiClient;
import com.unear.userservice.story.vo.UserAnalysisResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
