package com.zerobase.reserve.domain.common.builder;

import com.zerobase.reserve.domain.store.entity.Address;
import com.zerobase.reserve.domain.store.entity.SalesInfo;
import com.zerobase.reserve.domain.store.entity.Store;

import static com.zerobase.reserve.domain.common.constants.StoreConstants.*;
import static com.zerobase.reserve.domain.common.constants.StoreConstants.CLOSED_DAYS;

public class StoreBuilder {
    public static Store store() {
        Address address = Address.builder()
                .address(ADDRESS)
                .detailAddr(DETAIL_ADDR)
                .zipcode(ZIPCODE)
                .build();
        address.addCoordinate(129.055511349615, 35.1752550133221);

        SalesInfo salesInfo = SalesInfo.builder()
                .operatingStart(OPER_START)
                .operatingEnd(OPER_END)
                .closedDays(CLOSED_DAYS)
                .build();

        return Store.builder()
                .storeKey(STORE_KEY)
                .name(STORE_NAME)
                .description(DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .address(address)
                .salesInfo(salesInfo)
                .build();
    }
}
