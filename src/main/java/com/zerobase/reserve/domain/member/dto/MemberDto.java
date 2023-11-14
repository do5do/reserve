package com.zerobase.reserve.domain.member.dto;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.entity.Role;

public record MemberDto(
        String name,
        String email,
        Role role
) {
    public static MemberDto fromEntity(Member member) {
        return new MemberDto(member.getName(),
                member.getEmail(), member.getRole());
    }
}
