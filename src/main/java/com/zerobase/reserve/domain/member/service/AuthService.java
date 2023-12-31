package com.zerobase.reserve.domain.member.service;

import com.zerobase.reserve.domain.common.utils.KeyGenerator;
import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.dto.Signup;
import com.zerobase.reserve.domain.member.dto.model.MemberDto;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.reserve.global.exception.ErrorCode.MEMBER_ALREADY_EXISTS;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final KeyGenerator keyGenerator;

    /**
     * 회원가입<br>
     * 이미 등록된 회원인지 확인 후 회원가입을 진행합니다.
     *
     * @param request 가입 요청 정보
     * @return 가입한 유저 정보
     */
    @Transactional
    public MemberDto signup(Signup request) {
        validateMemberExists(request.getEmail());

        Member savedMember = memberRepository.save(
                request.toEntity(
                        keyGenerator.generateKey(),
                        passwordEncoder.encode(request.getPassword())));

        return MemberDto.fromEntity(savedMember);
    }

    private void validateMemberExists(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(MEMBER_ALREADY_EXISTS);
        }
    }

    /**
     * 로그인
     * 로그인 요청 정보를 spring security의 authenticate 메소드로 검증합니다.
     * (email 검증, 패스워드 매치)
     *
     * @param request 로그인 요청 정보
     * @return 로그인 정보
     */
    public MemberDto signin(Signin request) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));
        return MemberDto.fromEntity((Member) authentication.getPrincipal());
    }
}
