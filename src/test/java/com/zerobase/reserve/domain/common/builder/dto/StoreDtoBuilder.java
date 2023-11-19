package com.zerobase.reserve.domain.common.builder.dto;

import com.zerobase.reserve.domain.store.dto.AddressDto;
import com.zerobase.reserve.domain.store.dto.SalesInfoDto;
import com.zerobase.reserve.domain.store.dto.StoreDto;

import static com.zerobase.reserve.domain.common.constants.StoreConstants.*;

public class StoreDtoBuilder {
    public static StoreDto storeDto() {
        return StoreDto.builder()
                .storeId(STORE_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                .salesInfo(new SalesInfoDto(OPER_START, OPER_END, CLOSE_DAYS))
                .memberId(MEMBER_ID)
                .build();
    }
}
