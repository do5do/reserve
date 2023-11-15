package com.zerobase.reserve.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reserve.domain.member.dto.MemberDto;
import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.dto.Signup;
import com.zerobase.reserve.domain.member.entity.Role;
import com.zerobase.reserve.domain.member.service.AuthService;
import com.zerobase.reserve.global.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class)}
)
class AuthControllerTest {
    @MockBean
    AuthService authService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private final static String NAME = "name";
    private final static String EMAIL = "do@gmail.com";
    private final static String PASSWORD = "kimdo1234";
    private final static String PHONE_NUMBER = "010-1234-1234";

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.role")
                        .value("USER"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 부적절한 이메일 형식")
    void signup_invalid_email() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(NAME)
                .email("gmail.com")
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 부적절한 비밀번호 형식")
    void signup_invalid_password() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(NAME)
                .email(EMAIL)
                .password("kimdodo")
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 부적절한 enum 형식")
    void signup_invalid_role() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(memberDto());

        // when
        // then
        Signup signup = Signup.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .build();

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() throws Exception {
        // given
        given(authService.signin(any()))
                .willReturn(memberDto());

        // when
        // then
        Signin signin = new Signin(EMAIL, PASSWORD);

        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.role")
                        .value("USER"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 부적절한 이메일 형식")
    void signin_invalid_email() throws Exception {
        // given
        given(authService.signup(any()))
                .willReturn(memberDto());

        // when
        // then
        Signin signin = new Signin("gmail.com", PASSWORD);

        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    private static MemberDto memberDto() {
        return new MemberDto(NAME, EMAIL, Role.USER);
    }
}