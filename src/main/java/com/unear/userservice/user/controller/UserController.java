package com.unear.userservice.user.controller;

import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.common.security.CustomUser;
import com.unear.userservice.common.exception.exception.UserNotFoundException;
import com.unear.userservice.user.dto.response.MyStatisticsDetailResponseDto;
import com.unear.userservice.user.dto.response.MyStatisticsSummaryResponseDto;
import com.unear.userservice.user.dto.response.UserHistoryResponseDto;
import com.unear.userservice.user.dto.response.UserInfoResponseDto;
import com.unear.userservice.user.entity.User;
import com.unear.userservice.user.repository.UserRepository;
import com.unear.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final String USER_NOT_FOUND_MESSAGE = "사용자를 찾을 수 없습니다.";
    private static final String BARCODE_SUCCESS_MESSAGE = "바코드 조회 성공";

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/me/barcode")
    public ApiResponse<String> getMyBarcode(@AuthenticationPrincipal CustomUser user) {

        User dbuser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        return ApiResponse.success(BARCODE_SUCCESS_MESSAGE, dbuser.getBarcodeNumber());
    }

    @GetMapping("/me")
    public ApiResponse<UserInfoResponseDto> getMyInfo(@AuthenticationPrincipal CustomUser user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        UserInfoResponseDto dto = UserInfoResponseDto.from(dbUser);
        return ApiResponse.success("사용자 정보 조회 성공", dto);
    }

    @GetMapping("/me/statistics/summary")
    public ApiResponse<MyStatisticsSummaryResponseDto> getMyStatisticsSummary(@AuthenticationPrincipal CustomUser user) {
        MyStatisticsSummaryResponseDto summary = userService.getMySummary(user.getId());
        return ApiResponse.success("마이페이지 요약 통계 조회 성공", summary);
    }

    @GetMapping("/me/usage-history")
    public ApiResponse<Page<UserHistoryResponseDto>> getMyUsageHistory(
            @AuthenticationPrincipal CustomUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Page<UserHistoryResponseDto> result = userService.getUserUsageHistory(user.getId(), page, size);
        return ApiResponse.success("이용내역 조회 성공", result);
    }


    @GetMapping("/me/statistics/detail")
    public ApiResponse<MyStatisticsDetailResponseDto> getMyStatisticsDetail(
            @AuthenticationPrincipal CustomUser user,
            @RequestParam int year,
            @RequestParam int month) {

        MyStatisticsDetailResponseDto detail = userService.getMyStatisticsDetail(user.getId(), year, month);
        return ApiResponse.success("개인 통계 상세 조회 성공", detail);
    }

}
