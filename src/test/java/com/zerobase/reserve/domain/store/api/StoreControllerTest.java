package com.zerobase.reserve.domain.store.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reserve.domain.common.builder.dto.RegistRequestBuilder;
import com.zerobase.reserve.domain.common.builder.dto.StoreDtoBuilder;
import com.zerobase.reserve.domain.store.dto.EditRequest;
import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.StoresResponse;
import com.zerobase.reserve.domain.store.dto.model.AddressDto;
import com.zerobase.reserve.domain.store.dto.model.SalesInfoDto;
import com.zerobase.reserve.domain.store.service.StoreService;
import com.zerobase.reserve.global.config.SecurityConfig;
import com.zerobase.reserve.global.security.AuthenticationFilter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static com.zerobase.reserve.domain.common.constants.StoreConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = StoreController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class,
                SecurityAutoConfiguration.class
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                SecurityConfig.class, AuthenticationFilter.class
                        })
        }
)
class StoreControllerTest {
    @MockBean
    StoreService storeService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("매장 등록 성공")
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
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY))
                .andExpect(jsonPath("$.storeName").value(STORE_NAME))
                .andDo(print());
    }

    @Test
    @DisplayName("매장 등록 실패 - 부적절한 전화번호 형식")
    void registration_invalid_number() throws Exception {
        // given
        given(storeService.registration(any()))
                .willReturn(StoreDtoBuilder.storeDto());

        // when
        // then
        Registration.Request request =
                Registration.Request.builder()
                        .memberKey(MEMBER_KEY)
                        .storeName(STORE_NAME)
                        .description(DESCRIPTION)
                        .phoneNumber("123-123-12")
                        .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                        .salesInfo(new SalesInfoDto(LocalTime.MIN, LocalTime.MAX,
                                List.of("일요일")))
                        .build();

        mockMvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("키워드 검색")
    void searchKeyword() throws Exception {
        // given
        List<String> storeNames = List.of("맛있는 초밥", "더 맛있는 초밥집", "이것이 초밥이다");

        given(storeService.searchKeyword(anyString()))
                .willReturn(storeNames);

        // when
        // then
        mockMvc.perform(get("/api/v1/stores/search")
                        .param("keyword", "초밥"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeNames.length()")
                        .value(storeNames.size()))
                .andExpect(jsonPath("$.storeNames",
                        Matchers.containsInAnyOrder("맛있는 초밥",
                                "더 맛있는 초밥집", "이것이 초밥이다")))
                .andDo(print());
    }

    @Test
    @DisplayName("매장 정보 조회")
    void information() throws Exception {
        // given
        given(storeService.information(anyString()))
                .willReturn(StoreDtoBuilder.storeDto());

        // when
        // then
        mockMvc.perform(get("/api/v1/stores/" + STORE_KEY))
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY))
                .andExpect(jsonPath("$.name").value(STORE_NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE_NUMBER))
                .andExpect(jsonPath("$.address.address").value(ADDRESS))
                .andExpect(jsonPath("$.address.detailAddr").value(DETAIL_ADDR))
                .andExpect(jsonPath("$.address.zipcode").value(ZIPCODE))
                .andExpect(jsonPath("$.salesInfo.operatingStart")
                        .value("10:00:00"))
                .andExpect(jsonPath("$.salesInfo.operatingEnd")
                        .value("22:00:00"))
                .andExpect(jsonPath("$.salesInfo.closedDays",
                        Matchers.containsInAnyOrder("일요일")))
                .andDo(print());
    }

    @Test
    @DisplayName("매장 목록")
    void stores() throws Exception {
        // given
        StoresResponse response = StoresResponse.builder()
                .storeName(STORE_NAME)
                .address(ADDRESS)
                .score(2.3)
                .build();

        List<StoresResponse> contents = List.of(response);

        given(storeService.stores(any(), any()))
                .willReturn(new SliceImpl<>(contents));

        // when
        // then
        mockMvc.perform(get("/api/v1/stores")
                .param("x", "129.055511349615")
                .param("y", "35.1752550133221"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].storeName").value(STORE_NAME))
                .andExpect(jsonPath("$.content[0].address").value(ADDRESS))
                .andExpect(jsonPath("$.content[0].score").value(2.3))
                .andDo(print());
    }

    @Test
    @DisplayName("매장 수정")
    void edit_store() throws Exception {
        // given
        given(storeService.edit(any()))
                .willReturn(StoreDtoBuilder.storeDto());

        // when
        // then
        EditRequest request = EditRequest.builder()
                .storeKey(STORE_KEY)
                .storeName(STORE_NAME)
                .description(DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                .salesInfo(new SalesInfoDto(LocalTime.MIN, LocalTime.MAX,
                        List.of("일요일")))
                .build();

        mockMvc.perform(patch("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY))
                .andExpect(jsonPath("$.name").value(STORE_NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE_NUMBER))
                .andExpect(jsonPath("$.address.address").value(ADDRESS))
                .andExpect(jsonPath("$.address.detailAddr").value(DETAIL_ADDR))
                .andExpect(jsonPath("$.address.zipcode").value(ZIPCODE))
                .andExpect(jsonPath("$.salesInfo.operatingStart")
                        .value("10:00:00"))
                .andExpect(jsonPath("$.salesInfo.operatingEnd")
                        .value("22:00:00"))
                .andExpect(jsonPath("$.salesInfo.closedDays",
                        Matchers.containsInAnyOrder("일요일")))
                .andDo(print());
    }

    @Test
    @DisplayName("매장 삭제")
    void delete_store() throws Exception {
        // given
        given(storeService.delete(any()))
                .willReturn(STORE_KEY);

        // when
        // then
        mockMvc.perform(delete("/api/v1/stores/" + STORE_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeKey").value(STORE_KEY));
    }
}