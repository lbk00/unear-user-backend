package com.unear.userservice.auth.service.impl;

import ch.qos.logback.core.spi.ErrorCodes;
import com.unear.userservice.auth.dto.request.*;
import com.unear.userservice.auth.dto.response.LoginResponseDto;
import com.unear.userservice.auth.dto.response.LogoutResponseDto;
import com.unear.userservice.auth.dto.response.ProfileUpdateResponseDto;
import com.unear.userservice.auth.dto.response.RefreshResponseDto;
import com.unear.userservice.auth.dto.response.SignupResponseDto;
import com.unear.userservice.auth.service.AuthService;
import com.unear.userservice.auth.service.EmailService;
import com.unear.userservice.common.enums.LoginProvider;
import com.unear.userservice.common.exception.BusinessException;
import com.unear.userservice.common.exception.ErrorCode;
import com.unear.userservice.common.jwt.JwtTokenProvider;
import com.unear.userservice.common.jwt.RefreshTokenService;
import com.unear.userservice.common.response.ApiResponse;
import com.unear.userservice.common.security.CustomUser;
import com.unear.userservice.common.security.CustomUserDetailsService;
import com.unear.userservice.common.exception.exception.DuplicatedEmailException;
import com.unear.userservice.common.exception.exception.InvalidPasswordException;
import com.unear.userservice.common.exception.exception.InvalidTokenException;
import com.unear.userservice.common.exception.exception.UserNotFoundException;
import com.unear.userservice.user.entity.User;
import com.unear.userservice.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String RESET_PASSWORD_PREFIX = "resetVerified:";
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequest, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));

        log.info("일반 로그인 로그");
        System.out.println("일반 로그인 로그");
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
        }

        CustomUser customUser = new CustomUser(user);
        String accessToken = jwtTokenProvider.generateAccessToken(customUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUser);

        refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        LoginResponseDto dto = LoginResponseDto.of(user.getEmail(), accessToken);

        return ApiResponse.success("로그인 완료", dto);

    }

    @Override
    public ApiResponse<LoginResponseDto> oauthLogin(User user, HttpServletResponse response) {
        CustomUser customUser = new CustomUser(user);
        String accessToken = jwtTokenProvider.generateAccessToken(customUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUser);

        refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        LoginResponseDto dto = LoginResponseDto.of(user.getEmail(), accessToken);
        return ApiResponse.success("로그인 완료", dto);
    }



    @Override
    public ApiResponse<RefreshResponseDto> refresh(String refreshToken) {

        Long userId = jwtTokenProvider.extractUserId(refreshToken);

        if (!refreshTokenService.validateRefreshToken(userId, refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        CustomUser customUser = customUserDetailsService.loadUserByUserId(userId);

        String newAccessToken = jwtTokenProvider.generateAccessToken(customUser);

        RefreshResponseDto responseDto = new RefreshResponseDto(userId, newAccessToken);
        return ApiResponse.success("재발급 완료", responseDto);
    }

    @Override
    public ApiResponse<LogoutResponseDto> logout(String accessTokenHeader, HttpServletResponse response) {

        if (accessTokenHeader == null || !accessTokenHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("유효하지 않은 토큰 형식입니다");
        }

        String accessToken = accessTokenHeader.replace("Bearer ", "");
        Long userId = jwtTokenProvider.extractUserId(accessToken);

        refreshTokenService.deleteRefreshToken(userId);
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        LogoutResponseDto dto = new LogoutResponseDto(userId);

        return ApiResponse.success("로그아웃이 완료되었습니다", dto);

    }

    public ApiResponse<SignupResponseDto> signup(SignupRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicatedEmailException("이미 가입된 이메일입니다.");
        }
        String barcode = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        User user = User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .tel(dto.getTel())
                .birthdate(dto.getBirthdate())
                .gender(dto.getGender())
                .membershipCode("BASIC")
                .isProfileComplete(true)
                .barcodeNumber(barcode)
                .build();

        userRepository.save(user);

        SignupResponseDto responseDto = new SignupResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getBarcodeNumber()
        );

        return ApiResponse.success("회원가입 성공", responseDto);
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequestDto dto) {
        String key = "resetVerified:" + dto.getEmail();
        String verified = redisTemplate.opsForValue().get(key);

        if (!"true".equals(verified)) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        // 비밀번호 재설정 완료 후 인증 상태 삭제
        redisTemplate.delete(key);
    }

    @Override
    public ApiResponse<ProfileUpdateResponseDto> completeOAuthProfile(Long userId, CompleteProfileRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));

        if (user.getProvider() == LoginProvider.EMAIL) {
            throw new RuntimeException("OAuth 유저만 가능합니다");
        }

        String barcode = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        user.setUsername(request.getUsername());
        user.setTel(request.getTel());
        user.setBirthdate(request.getBirthdate());
        user.setGender(request.getGender());
        user.setBarcodeNumber(barcode);
        user.setProfileComplete(true);

        userRepository.save(user);

        return ApiResponse.success("프로필 완성", ProfileUpdateResponseDto.from(user));
    }

    @Override
    public User getUserFromAccessToken(String accessToken) {
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new InvalidTokenException("액세스 토큰이 유효하지 않습니다");
        }
        Long userId = jwtTokenProvider.extractUserId(accessToken);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
    }

    @Transactional
    @Override
    public void changePassword(Long userId, ChangePasswordRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        user.changePassword(dto.getNewPassword(), passwordEncoder);
    }

    @Override
    public String findMaskedEmailByTel(String tel) {
        User user = userRepository.findByTel(tel)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        return maskEmail(user.getEmail());
    }
    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
        }
        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);
        int maskLength = Math.max(1, (int) (local.length() * 0.3));
        String visible = local.substring(0, local.length() - maskLength);
        String masked = "*".repeat(maskLength);
        return visible + masked + "@" + domain;
    }

    @Override
    public void sendResetPasswordCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
        SecureRandom random = new SecureRandom();
        String code = String.format("%06d", random.nextInt(1000000));
        redisTemplate.opsForValue().set(
                RESET_PASSWORD_PREFIX + email,
                code,
                Duration.ofMinutes(5)
        );
        emailService.sendResetPasswordCode(email, code);
    }

    @Override
    public void verifyResetPasswordCode(VerifyResetPasswordCodeRequestDto dto) {
        String key = RESET_PASSWORD_PREFIX + dto.getEmail();
        String savedCode = redisTemplate.opsForValue().get(key);

        if (savedCode == null || !savedCode.equals(dto.getCode())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 인증 성공 → 인증 상태 저장 (선택)
        redisTemplate.opsForValue().set(RESET_PASSWORD_PREFIX + dto.getEmail(), "true", Duration.ofMinutes(10));
    }
}
