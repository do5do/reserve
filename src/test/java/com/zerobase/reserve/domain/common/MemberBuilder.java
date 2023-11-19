package com.zerobase.reserve.domain.common;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.entity.Role;

import static com.zerobase.reserve.domain.common.MemberConstants.*;
import static com.zerobase.reserve.domain.common.MemberConstants.PHONE_NUMBER;

public class MemberBuilder {
    public static Member member() {
        return Member.builder()
                .memberId(MEMBER_ID)
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();
    }
}
