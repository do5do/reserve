package com.zerobase.reserve.domain.member.service;

import com.zerobase.reserve.domain.common.MemberBuilder;
import com.zerobase.reserve.domain.common.util.KeyGenerator;
import com.zerobase.reserve.domain.member.dto.MemberDto;
import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.dto.Signup;
import com.zerobase.reserve.domain.member.entity.Role;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.repository.MemberRepository;
import com.zerobase.reserve.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.zerobase.reserve.domain.common.MemberConstants.*;
import static com.zerobase.reserve.domain.common.StoreConstants.MEMBER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    KeyGenerator keyGenerator;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given
        given(memberRepository.save(any()))
                .willReturn(MemberBuilder.member());

        given(keyGenerator.generateKey())
                .willReturn(MEMBER_ID);

        given(passwordEncoder.encode(any()))
                .willReturn(PASSWORD);

        // when
        MemberDto memberDto = memberService.signup(Signup.builder().build());

        // then
        assertEquals(MEMBER_ID, memberDto.getMemberId());
        assertEquals(NAME, memberDto.getName());
        assertEquals(EMAIL, memberDto.getEmail());
        assertEquals(Role.USER, memberDto.getRole());
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 있는 회원")
    void signup_member_already_exists() {
        // given
        given(memberRepository.existsByEmail(any()))
                .willReturn(true);

        // when
        MemberException exception = assertThrows(MemberException.class, () ->
                memberService.signup(Signup.builder().build()));

        // then
        assertEquals(ErrorCode.MEMBER_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(MemberBuilder.member());

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        given(authenticationManagerBuilder.getObject())
                .willReturn(mock(AuthenticationManager.class));

        given(authenticationManagerBuilder.getObject().authenticate(any()))
                .willReturn(authentication);

        // when
        MemberDto memberDto = memberService.signin(new Signin(EMAIL, PASSWORD));

        // then
        assertEquals(NAME, memberDto.getName());
        assertEquals(EMAIL, memberDto.getEmail());
        assertEquals(Role.USER, memberDto.getRole());
    }
}