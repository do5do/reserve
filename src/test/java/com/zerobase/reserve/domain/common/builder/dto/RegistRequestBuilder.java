package com.zerobase.reserve.domain.common.builder.dto;

import com.zerobase.reserve.domain.store.dto.model.AddressDto;
import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.model.SalesInfoDto;

import static com.zerobase.reserve.domain.common.constants.StoreConstants.*;
import static com.zerobase.reserve.domain.common.constants.StoreConstants.CLOSED_DAYS;

public class RegistRequestBuilder {
    public static Registration.Request registRequest() {
        return Registration.Request.builder()
                        .memberKey(MEMBER_KEY)
                        .storeName(STORE_NAME)
                        .description(DESCRIPTION)
                        .phoneNumber(PHONE_NUMBER)
                        .address(new AddressDto(ADDRESS, DETAIL_ADDR, ZIPCODE))
                        .salesInfo(new SalesInfoDto(OPER_START, OPER_END, CLOSED_DAYS))
                        .build();
    }
}
