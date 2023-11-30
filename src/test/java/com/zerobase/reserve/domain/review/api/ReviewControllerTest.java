package com.zerobase.reserve.domain.review.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reserve.domain.common.builder.dto.ReviewDtoBuilder;
import com.zerobase.reserve.domain.review.dto.Update;
import com.zerobase.reserve.domain.review.dto.Write;
import com.zerobase.reserve.domain.review.service.ReviewService;
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

import static com.zerobase.reserve.domain.common.constants.ReservationConstants.RESERVATION_KEY;
import static com.zerobase.reserve.domain.common.constants.ReviewConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@EnableMethodSecurity
class ReviewControllerTest {
    @MockBean
    ReviewService reviewService;

    @MockBean
    TokenProvider tokenProvider;

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
    @DisplayName("리뷰 작성")
    @WithMockUser(username = "do@gmail.com", roles = "USER")
    void write() throws Exception { // security를 사용하는 테스트로 진행하자
        // given
        given(reviewService.write(any(), any()))
                .willReturn(ReviewDtoBuilder.reviewDto());

        // when
        // then
        mockMvc.perform(post("/api/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Write.Request.builder()
                                .reservationKey(RESERVATION_KEY)
                                .contents(CONTENTS)
                                .score(SCORE)
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId")
                        .value(ID))
                .andDo(print());
    }

    @Test
    @DisplayName("리뷰 수정")
    void update() throws Exception {
        // given
        given(reviewService.update(any()))
                .willReturn(ReviewDtoBuilder.reviewDto());

        // when
        // then
        mockMvc.perform(patch("/api/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Update.Request.builder()
                                .reviewId(ID)
                                .contents(CONTENTS)
                                .score(SCORE)
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId")
                        .value(ID))
                .andDo(print());
    }

    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() throws Exception {
        // given
        given(reviewService.delete(any()))
                .willReturn(ReviewDtoBuilder.reviewDto());

        // when
        // then
        mockMvc.perform(delete("/api/v1/reviews/" + ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId")
                        .value(ID))
                .andDo(print());
    }
}