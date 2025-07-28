package com.unear.userservice.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindEmailRequestDto {

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String tel;
}