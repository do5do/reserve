package com.zerobase.reserve.domain.member.dto;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.entity.Role;
import com.zerobase.reserve.domain.common.validator.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Signup(
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "[a-zA-Z1-9]{8,16}",
                message = "비밀번호는 영어와 숫자를 포함해서 8~16자리 입니다.")
        String password,

        @NotBlank
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
                message = "핸드폰 번호의 약식과 맞지 않습니다. ex) 010-1234-1234")
        String phoneNumber,

        @ValidEnum(enumClass = Role.class)
        Role role
) {
    public Member toEntity(String password) {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }
}
