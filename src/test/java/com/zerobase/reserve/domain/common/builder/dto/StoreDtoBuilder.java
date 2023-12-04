package com.zerobase.reserve.domain.common.builder.dto;

import com.zerobase.reserve.domain.store.dto.model.AddressDto;
import com.zerobase.reserve.domain.store.dto.model.SalesInfoDto;
import com.zerobase.reserve.domain.store.dto.model.StoreDto;

import static com.zerobase.reserve.domain.common.constants.StoreConstants.*;

public class StoreDtoBuilder {
    public static StoreDto storeDto() {
        return StoreDto.builder()
                .storeKey(STORE_KEY)
                .name(STORE_NAME)
                .description(DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                .salesInfo(new SalesInfoDto(OPER_START, OPER_END, CLOSED_DAYS))
                .build();
    }
}
