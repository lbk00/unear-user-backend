package com.unear.userservice.common.docs.story;

import com.unear.userservice.story.dto.response.StoryCurrentResponseDto;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;
import java.lang.reflect.Method;

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
                                            [
                                                 {
                                                     "imageUrl": "LIFE1.png",
                                                     "comment": "바쁜 일상 속에서,\\n잠시의 여유를 찾았어요.",
                                                     "date": "2025-07-12",
                                                     "storeName": "GS25 화곡61길점",
                                                     "amount": 34007,
                                                     "logoUrl": "LIFE"
                                                 },
                                                 {
                                                     "imageUrl": "BEAUTY1.png",
                                                     "comment": "아름다움에 투자한 오늘,\\n기분 좋은 변화가 시작돼요.",
                                                     "date": "2025-07-27",
                                                     "storeName": "유앤아이피부과의원 선릉점",
                                                     "amount": 30191,
                                                     "logoUrl": "LIFE"
                                                 },
                                                 {
                                                     "imageUrl": "BEAUTY1.png",
                                                     "comment": "피부에 양보하는 순간,\\n자신감도 함께 빛났어요.",
                                                     "date": "2025-07-18",
                                                     "storeName": "유앤아이피부과의원 잠실점",
                                                     "amount": 28043,
                                                     "logoUrl": "ACTIVITY"
                                                 },
                                                 {
                                                     "imageUrl": "ACTIVITY2.png",
                                                     "comment": "몸도 마음도 가벼워지는,\\n활동적인 하루였어요.",
                                                     "date": "2025-07-23",
                                                     "storeName": "SEOUL SKY",
                                                     "amount": 27451,
                                                     "logoUrl": "LIFE"
                                                 }
                                             ]
                                        """
                            )
                    }
            )
    )
    @SecurityRequirement(name = "BearerAuth")
    public @interface GetStory {}


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Operation(
            summary = "소비 스토리 저장",
            description = "소비 분석 결과(대표 결제 내역, AI 코멘트 등)를 저장합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 저장 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "스토리 저장 성공",
                                    value = """
                                        {
                                          "success": true,
                                          "codeName": "SUCCESS",
                                          "message": "스토리 저장 완료",
                                          "data": null
                                        }
                                        """
                            )
                    }
            )
    )
    @SecurityRequirement(name = "BearerAuth")
    public @interface CreateStory {}


}
