package com.unear.userservice.common.docs.stamp;

import com.unear.userservice.stamp.dto.response.EventStampResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

public class StampApiDocs {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Operation(
            summary = "이벤트 스탬프 상태 조회",
            description = "로그인한 유저 기준으로, 해당 이벤트에서 스탬프를 찍은 매장 목록과 룰렛 가능 여부를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스탬프 상태 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EventStampResponseDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "Stamp Status",
                                    summary = "로그인 유저의 스탬프 상태 및 룰렛 가능 여부 조회",
                                    value = """
                                            {
                                              "resultCode": 200,
                                              "codeName": "SUCCESS",
                                              "message": "스탬프 조회 성공",
                                              "data": {
                                                "stamps": [
                                                  {
                                                    "eventPlaceId": 77,
                                                    "placeName": "성수동 팝업스토어",
                                                    "eventCode": "이벤트(필수)",
                                                    "stamped": true
                                                  },
                                                  {
                                                    "eventPlaceId": 80,
                                                    "placeName": "나이스웨더",
                                                    "eventCode": "이벤트(일반)",
                                                    "stamped": true
                                                  },
                                                  {
                                                    "eventPlaceId": 81,
                                                    "placeName": "밀로커피로스터스",
                                                    "eventCode": "이벤트(일반)",
                                                    "stamped": true
                                                  },
                                                  {
                                                    "eventPlaceId": 82,
                                                    "placeName": "땡스오트",
                                                    "eventCode": "이벤트(일반)",
                                                    "stamped": true
                                                  }
                                                ],
                                                "rouletteAvailable": true
                                              }
                                            }
                                            """
                            )
                    }
            )
    )
    @SecurityRequirement(name = "BearerAuth")
    public @interface GetMyStampStatus {}
}
