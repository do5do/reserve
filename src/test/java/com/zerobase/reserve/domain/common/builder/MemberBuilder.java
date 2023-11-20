package com.zerobase.reserve.domain.common.builder;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.entity.Role;

import static com.zerobase.reserve.domain.common.constants.MemberConstants.*;
import static com.zerobase.reserve.domain.common.constants.MemberConstants.PHONE_NUMBER;

public class MemberBuilder {
    public static Member member() {
        return Member.builder()
                .memberKey(MEMBER_KEY)
                .name(MEMBER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();
    }
}
