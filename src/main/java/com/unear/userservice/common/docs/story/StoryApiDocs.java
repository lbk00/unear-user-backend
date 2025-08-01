package com.unear.userservice.common.docs.story;

import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

public class StoryApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Operation(
            summary = "소비 진단 결과 조회",
            description = "사용자의 최근 소비 내역을 기반으로 AI가 생성한 결제 타입과 코멘트를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "소비 진단 결과 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StoryDiagnosisResponseDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "AI 소비 진단 예시",
                                    summary = "진단 결과 예시",
                                    value = """
                                            {
                                              "resultCode": 200,
                                              "codeName": "SUCCESS",
                                              "message": "소비 진단 결과 조회 성공",
                                              "data": {
                                                "type": "쩝쩝박사",
                                                "comment": "축제의 즐거움은 맛에 있다고 믿는 당신! 다양한 먹거리를 탐험하며..."
                                              }
                                            }
                                            """
                            )
                    }
            )
    )
    @SecurityRequirement(name = "BearerAuth")
    public @interface GetDiagnosis {}
}
