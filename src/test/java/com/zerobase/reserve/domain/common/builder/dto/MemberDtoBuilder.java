package com.zerobase.reserve.domain.common.builder.dto;

import com.zerobase.reserve.domain.member.dto.MemberDto;
import com.zerobase.reserve.domain.member.entity.Role;

import static com.zerobase.reserve.domain.common.constants.MemberConstants.EMAIL;
import static com.zerobase.reserve.domain.common.constants.MemberConstants.MEMBER_NAME;

public class MemberDtoBuilder {
    public static MemberDto memberDto() {
        return MemberDto.builder()
                .name(MEMBER_NAME)
                .email(EMAIL)
                .role(Role.USER)
                .build();
    }
}
