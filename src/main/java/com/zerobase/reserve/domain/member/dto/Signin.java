package com.zerobase.reserve.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Signin(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "[a-zA-Z1-9]{8,16}",
                message = "비밀번호는 영어와 숫자를 포함해서 8~16자리 입니다.")
        String password
) {
}
