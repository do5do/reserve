package com.zerobase.reserve.domain.common;

import com.zerobase.reserve.domain.store.entity.Address;
import com.zerobase.reserve.domain.store.entity.SalesInfo;
import com.zerobase.reserve.domain.store.entity.Store;

import static com.zerobase.reserve.domain.common.StoreConstants.*;
import static com.zerobase.reserve.domain.common.StoreConstants.CLOSE_DAYS;

public class StoreBuilder {
    public static Store store() {
        return Store.builder()
                .storeId(STORE_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .address(new Address(ADDRESS, DETAIL_ADDR, ZIPCODE))
                .salesInfo(new SalesInfo(OPER_START, OPER_END, CLOSE_DAYS))
                .build();
    }
}
