package com.zerobase.reserve.domain.store.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reserve.domain.common.builder.dto.RegistRequestBuilder;
import com.zerobase.reserve.domain.common.builder.dto.StoreDtoBuilder;
import com.zerobase.reserve.domain.member.service.MemberService;
import com.zerobase.reserve.domain.store.service.StoreService;
import com.zerobase.reserve.global.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.zerobase.reserve.domain.common.constants.StoreConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@EnableMethodSecurity
public class StoreControllerSecurityTest {
    @MockBean
    MemberService memberService;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    StoreService storeService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @WithMockUser(username = "kim", password = "kimdo1234", roles = "MANAGER")
    @DisplayName("매니저 권한 - 매장 등록 성공")
    void registration_success() throws Exception {
        // given
        given(storeService.registration(any()))
                .willReturn(StoreDtoBuilder.storeDto());

        // when
        // then
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RegistRequestBuilder.registRequest()))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.memberKey").value(MEMBER_KEY))
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY))
                .andExpect(jsonPath("$.storeName").value(STORE_NAME))
                .andDo(print());
    }
}
