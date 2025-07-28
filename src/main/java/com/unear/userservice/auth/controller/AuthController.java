package com.unear.userservice.auth.controller;

import com.unear.userservice.auth.dto.request.*;
import com.unear.userservice.auth.dto.response.*;
import com.unear.userservice.auth.service.AuthService;
import com.unear.userservice.auth.service.EmailService;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.common.exception.ErrorCode;
import com.unear.userservice.common.security.CustomUser;
import com.unear.userservice.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/oauth/complete-profile")
    public ResponseEntity<ApiResponse<ProfileUpdateResponseDto>> completeOAuthProfile (
            @AuthenticationPrincipal CustomUser user,
            @Valid @RequestBody CompleteProfileRequestDto request) {

        Long userId = user.getUser().getUserId();
        return ResponseEntity.ok(authService.completeOAuthProfile(userId, request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponseDto>> logout(@RequestHeader("Authorization") String accessToken, HttpServletResponse response) {
        return ResponseEntity.ok(authService.logout(accessToken, response));
    }

    @GetMapping("/hash/{password}")
    public String generateHash(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("이미 가입된 이메일입니다.");
        }
        String code = emailService.generateCode();
        emailService.sendEmail(email, code);
        emailService.saveCode(email, code);
        return ResponseEntity.ok("이메일로 인증코드가 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean match = emailService.verifyCode(email, code);
        if (match) {
            return ResponseEntity.ok("인증 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDto dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok(ApiResponse.success("비밀번호가 재설정되었습니다."));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal CustomUser user,
            @RequestBody @Valid ChangePasswordRequestDto requestDto
    ) {
        authService.changePassword(user.getUser().getUserId(), requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-id")
    public ResponseEntity<MaskedEmailResponseDto> findEmailByTel(
            @RequestBody @Valid FindEmailRequestDto requestDto
    ) {
        String maskedEmail = authService.findMaskedEmailByTel(requestDto.getTel());
        return ResponseEntity.ok(new MaskedEmailResponseDto(maskedEmail));
    }

    @PostMapping("/reset-password/send-code")
    public ResponseEntity<Void> sendResetPasswordCode(
            @RequestBody @Valid ResetPasswordEmailRequestDto requestDto
    ) {
        authService.sendResetPasswordCode(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/verify")
    public ResponseEntity<ApiResponse<String>> verifyResetPasswordCode(
            @RequestBody @Valid VerifyResetPasswordCodeRequestDto dto) {
        authService.verifyResetPasswordCode(dto);
        return ResponseEntity.ok(ApiResponse.success("인증코드 검증 성공"));
    }


}
