package com.zerobase.reserve.domain.store.service;

import com.zerobase.reserve.domain.common.builder.MemberBuilder;
import com.zerobase.reserve.domain.common.builder.StoreBuilder;
import com.zerobase.reserve.domain.common.util.KeyGenerator;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.repository.MemberRepository;
import com.zerobase.reserve.domain.store.dto.AddressDto;
import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.SalesInfoDto;
import com.zerobase.reserve.domain.store.dto.StoreDto;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.exception.StoreException;
import com.zerobase.reserve.domain.store.repository.StoreRepository;
import com.zerobase.reserve.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.zerobase.reserve.domain.common.constants.StoreConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @Mock
    StoreRepository storeRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    KeyGenerator keyGenerator;

    @InjectMocks
    StoreService storeService;

    @Test
    @DisplayName("매장 등록 성공")
    void registration_success() {
        // given
        given(memberRepository.findByMemberKey(anyString()))
                .willReturn(Optional.of(MemberBuilder.member()));

        given(storeRepository.save(any()))
                .willReturn(StoreBuilder.store());

        given(keyGenerator.generateKey())
                .willReturn(STORE_KEY);

        // when
        StoreDto storeDto = storeService.registration(
                Registration.Request.builder()
                        .memberKey(MEMBER_KEY)
                        .storeName(STORE_NAME)
                        .description(DESCRIPTION)
                        .phoneNumber(PHONE_NUMBER)
                        .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                        .salesInfo(new SalesInfoDto(OPER_START, OPER_END,
                                CLOSE_DAYS))
                        .build());

        // then
        assertEquals(STORE_KEY, storeDto.getStoreKey());
        assertEquals(STORE_NAME, storeDto.getName());
        assertEquals(DESCRIPTION, storeDto.getDescription());
    }

    @Test
    @DisplayName("매장 등록 실패 - 존재하지 않는 회원")
    void registration_member_not_found() {
        // given
        given(memberRepository.findByMemberKey(any()))
                .willReturn(Optional.empty());

        // when
        MemberException exception = assertThrows(MemberException.class, () ->
                storeService.registration(
                        Registration.Request.builder().build()));

        // then
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("매장 검색 성공")
    void searchKeyword_success() {
        // given
        String keyword = "초밥";
        Pageable limit = PageRequest.of(0, 50);
        Page<Store> stores = new PageImpl<>(stores(), limit, 1);

        given(storeRepository.findAllByNameContains(keyword, limit))
                .willReturn(stores);

        // when
        List<String> storeNames = storeService.searchKeyword(keyword);

        // then
        assertEquals(3, storeNames.size());
    }

    private static List<Store> stores() {
        return List.of(
                Store.builder().name("맛있는 초밥").build(),
                Store.builder().name("더 맛있는 초밥집").build(),
                Store.builder().name("이것이 초밥이다").build()
        );
    }

    @Test
    @DisplayName("매장 조회 성공")
    void information_success() {
        // given
        Store store = StoreBuilder.store();
        store.setMember(MemberBuilder.member());

        given(storeRepository.findByStoreKey(STORE_KEY))
                .willReturn(Optional.of(store));

        // when
        StoreDto storeDto = storeService.information(STORE_KEY);

        // then
        assertEquals(STORE_KEY, storeDto.getStoreKey());
    }

    @Test
    @DisplayName("매장 조회 실패 - 존재하지 않는 매장")
    void information_store_not_found() {
        // given
        given(storeRepository.findByStoreKey(STORE_KEY))
                .willReturn(Optional.empty());

        // when
        StoreException exception = assertThrows(StoreException.class, () ->
                storeService.information(STORE_KEY));

        // then
        assertEquals(ErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
    }
}