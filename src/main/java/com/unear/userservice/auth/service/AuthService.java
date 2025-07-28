package com.unear.userservice.auth.service;

import com.unear.userservice.auth.dto.request.*;
import com.unear.userservice.auth.dto.response.LoginResponseDto;
import com.unear.userservice.auth.dto.response.LogoutResponseDto;
import com.unear.userservice.auth.dto.response.ProfileUpdateResponseDto;
import com.unear.userservice.auth.dto.response.RefreshResponseDto;
import com.unear.userservice.auth.dto.response.SignupResponseDto;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface AuthService {
    ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response);

    ApiResponse<LoginResponseDto> oauthLogin(User user, HttpServletResponse response);


    ApiResponse<RefreshResponseDto> refresh(String refreshTokenHeader);

    ApiResponse<LogoutResponseDto> logout(String accessTokenHeader, HttpServletResponse response);

    ApiResponse<SignupResponseDto> signup(SignupRequestDto dto);

    void resetPassword(ResetPasswordRequestDto request);

    ApiResponse<ProfileUpdateResponseDto> completeOAuthProfile(Long userId, @Valid CompleteProfileRequestDto request);

    User getUserFromAccessToken(String accessToken);

    void changePassword(Long userId, ChangePasswordRequestDto dto); // 비밀번호 변경

    String findMaskedEmailByTel(String tel);

    void sendResetPasswordCode(String email);   // 비밀번호 재설정 인증코드 보내기

    void verifyResetPasswordCode(VerifyResetPasswordCodeRequestDto dto); // 인증코드 검증
}
