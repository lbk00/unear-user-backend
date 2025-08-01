package com.unear.userservice.common.docs.story;

import com.unear.userservice.story.dto.response.StoryCurrentResponseDto;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.dto.response.StoryHistoryResponseDto;
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


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Operation(
            summary = "소비 히스토리 조회",
            description = "사용자의 최근 3달 소비 내역을 기반으로 스토리와 코멘트를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "소비 히스토리 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StoryHistoryResponseDto.class,
                                     type = "array"),
                    examples = {
                            @ExampleObject(
                                    name = "소비 히스토리 조회 예시",
                                    summary = "조회 결과 예시",
                                    value = """
                                            {
                                                "resultCode": 200,
                                                "codeName": "SUCCESS",
                                                "message": "요청이 성공적으로 처리되었습니다.",
                                                "data": [
                                                    {
                                                        "month": "2025-08",
                                                        "type": "쩝쩝박사",
                                                        "comment": "축제의 즐거움은 맛에 있다고 믿는 당신!",
                                                        "imageUrl": "https://cdn.unear.site/story/type/zzep.png"
                                                    },
                                                    {
                                                        "month": "2025-07",
                                                        "type": "알뜰소비러",
                                                        "comment": "혜택은 놓치지 않는다! 알뜰하고 계획적인 당신!",
                                                        "imageUrl": "https://cdn.unear.site/story/type/saver.png"
                                                    }
                                                ]
                                            }
                                            """
                            )
                    }
            )
    )
    @SecurityRequirement(name = "BearerAuth")
    public @interface GetHistory {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Operation(
            summary = "이번 달 스토리 조회",
            description = "한 줄 멘트 + 대표 결제 내역 + 일러스트 이미지를 포함한 스토리 상세 화면에 사용됩니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "이번 달 스토리 응답",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StoryCurrentResponseDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "스토리 상세 예시",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "month": "2025-07",
                                                "comment": "맛있는 음식 도착 소식에, 행복해지는 순간이 찾아왔어.",
                                                "imageUrl": "https://cdn.unear.site/story/current/zzep_full.png",
                                                "representativeLog": {
                                                  "date": "2025-07-15",
                                                  "storeName": "맥도날드 치즈버거",
                                                  "amount": 14200,
                                                  "logoUrl": "https://cdn.unear.site/logo/mcdonalds.png"
                                                }
                                              }
                                            }
                                            """
                            )
                    }
            )
    )
    @SecurityRequirement(name = "BearerAuth")
    public @interface GetCurrent {}
}
