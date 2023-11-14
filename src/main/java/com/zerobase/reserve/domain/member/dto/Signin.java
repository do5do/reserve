package com.zerobase.reserve.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Signin(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
