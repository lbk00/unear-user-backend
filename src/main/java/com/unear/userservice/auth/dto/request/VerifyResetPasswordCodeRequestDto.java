package com.unear.userservice.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyResetPasswordCodeRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String code;
}