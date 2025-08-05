package com.unear.userservice.common.docs.recommend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

public class RecommendApiDocs {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Operation(summary = "위치 기반 추천 장소 조회", description = "사용자의 현재 위치를 기반으로 개인화된 추천 장소 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "추천 장소 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.unear.userservice.recommend.dto.response.RecommendPlaceResponseDto.class), examples = @ExampleObject(value = """
            {
                  "resultCode": 200,
                  "codeName": "SUCCESS",
                  "message": "요청이 성공적으로 처리되었습니다.",
                  "data": [
                      {
                      "placeId": 452,
                                  "placeName": "GS 25 성수사랑점",
                                  "latitude": 37.5448616,
                                  "longitude": 127.0542854,
                                  "distanceInMeters": 237.09456468,
                                  "score": 0.2548
                              },
                              {
                                  "placeId": 449,
                                  "placeName": "세븐도어즈",
                                  "latitude": 37.5421084,
                                  "longitude": 127.0597260,
                                  "distanceInMeters": 345.5809482,
                                  "score": 0.2433
                              },
                              {
                                  "placeId": 457,
                                  "placeName": "CU 성수성문점",
                                  "latitude": 37.5387510,
                                  "longitude": 127.0562380,
                                  "distanceInMeters": 610.51506948,
                                  "score": 0.2223
                              }
                          ]
            """)))
    @SecurityRequirement(name = "BearerAuth")
    public @interface GetRecommendPlaces {
    }
}
