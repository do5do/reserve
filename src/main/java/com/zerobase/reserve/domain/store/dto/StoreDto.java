package com.zerobase.reserve.domain.store.dto;

import com.zerobase.reserve.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private String storeKey;
    private String name;
    private String description;
    private String phoneNumber;
    private AddressDto address;
    private SalesInfoDto salesInfo;
    private String memberKey;

    public static StoreDto fromEntity(Store store) {
        return StoreDto.builder()
                .storeKey(store.getStoreKey())
                .name(store.getName())
                .description(store.getDescription())
                .phoneNumber(store.getPhoneNumber())
                .address(AddressDto.fromEntity(store.getAddress()))
                .salesInfo(SalesInfoDto.fromEntity(store.getSalesInfo()))
                .memberKey(store.getMember().getMemberKey())
                .build();
    }
}
