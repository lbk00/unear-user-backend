package com.unear.userservice.auth.service.impl;

import com.unear.userservice.common.enums.LoginProvider;
import com.unear.userservice.user.entity.User;
import com.unear.userservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("네이버 loadUser 시작");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> response = oAuth2User.getAttribute("response");

        if (response == null) {
            throw new OAuth2AuthenticationException("네이버 사용자 정보가 없습니다.");
        }

        String email = (String) response.get("email");
        String name = (String) response.get("name");
        String naverId = (String) response.get("id");

        log.info("네이버 로그인 email={}, name={}, id={}", email, name, naverId);

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not provided by Naver");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .username(name)
                                .password(null)
                                .provider(LoginProvider.NAVER)   // enum 처리된 경우
                                .providerId(naverId)
                                .tel(null)
                                .birthdate(null)
                                .gender(null)
                                .membershipCode("BASIC")
                                .isProfileComplete(false)
                                .barcodeNumber(null)
                                .build()
                ));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                response,
                "email"
        );
    }
}

