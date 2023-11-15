package com.zerobase.reserve.domain.member.service;

import com.zerobase.reserve.domain.member.dto.MemberDto;
import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.dto.Signup;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.entity.Role;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.repository.MemberRepository;
import com.zerobase.reserve.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthService authService;

    private final static String NAME = "name";
    private final static String EMAIL = "do@gmail.com";
    private final static String PASSWORD = "kimdo1234";
    private final static String PHONE_NUMBER = "010-1234-1234";

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given
        given(memberRepository.save(any()))
                .willReturn(member());

        // when
        MemberDto memberDto = authService.signup(Signup.builder().build());

        // then
        assertEquals(NAME, memberDto.name());
        assertEquals(EMAIL, memberDto.email());
        assertEquals(Role.USER, memberDto.role());
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 있는 회원")
    void signup_member_already_exists() {
        // given
        given(memberRepository.existsByEmail(any()))
                .willReturn(true);

        // when
        MemberException exception = assertThrows(MemberException.class, () ->
                authService.signup(Signup.builder().build()));

        // then
        assertEquals(ErrorCode.MEMBER_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {
        // given
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(member());

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        given(authenticationManager.authenticate(any()))
                .willReturn(authentication);

        // when
        MemberDto memberDto = authService.signin(new Signin(EMAIL, PASSWORD));

        // then
        assertEquals(NAME, memberDto.name());
        assertEquals(EMAIL, memberDto.email());
        assertEquals(Role.USER, memberDto.role());
    }

    private static Member member() {
        return Member.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();
    }

}