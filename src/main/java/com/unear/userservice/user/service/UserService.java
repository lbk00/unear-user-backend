package com.unear.userservice.user.service;


import com.unear.userservice.user.dto.response.MyStatisticsDetailResponseDto;
import com.unear.userservice.user.dto.response.MyStatisticsSummaryResponseDto;
import com.unear.userservice.user.dto.response.UserHistoryResponseDto;
import com.unear.userservice.user.dto.response.UserInfoResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {
    UserInfoResponseDto getUserInfo(Long userId);
    String getMembershipBarcode(Long userId);
    MyStatisticsSummaryResponseDto getMySummary(Long userId);
    Page<UserHistoryResponseDto> getUserUsageHistory(Long userId, int page, int size);
    MyStatisticsDetailResponseDto getMyStatisticsDetail(Long userId, int year, int month);

}