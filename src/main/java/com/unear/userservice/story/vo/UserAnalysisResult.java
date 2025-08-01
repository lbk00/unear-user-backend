package com.unear.userservice.story.vo;

import lombok.Builder;

@Builder
public record UserAnalysisResult(
        double foodRate,
        int avgAmount,
        String activitySummary
) {}
