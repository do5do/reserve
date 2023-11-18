package com.zerobase.reserve.domain.member.service;

import com.zerobase.reserve.domain.common.util.KeyGenerator;
import com.zerobase.reserve.domain.member.dto.MemberDto;
import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.dto.Signup;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.reserve.global.exception.ErrorCode.MEMBER_ALREADY_EXISTS;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public MemberDto signup(Signup request) {
        validateMemberExists(request.getEmail());

        Member savedMember = memberRepository.save(
                request.toEntity(
                        KeyGenerator.generateKey(),
                        passwordEncoder.encode(request.getPassword())));

        return MemberDto.fromEntity(savedMember);
    }

    private void validateMemberExists(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(MEMBER_ALREADY_EXISTS);
        }
    }

    public MemberDto signin(Signin request) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return MemberDto.fromEntity((Member) authentication.getPrincipal());
    }
}
