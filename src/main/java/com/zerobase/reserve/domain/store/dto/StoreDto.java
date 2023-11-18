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
    private String storeId;
    private String name;
    private String description;
    private AddressDto address;
    private SalesInfoDto salesInfo;
    private String memberId;

    public static StoreDto fromEntity(Store store) {
        return StoreDto.builder()
                .storeId(store.getStoreId())
                .name(store.getName())
                .description(store.getDescription())
                .address(AddressDto.fromEntity(store.getAddress()))
                .salesInfo(SalesInfoDto.fromEntity(store.getSalesInfo()))
                .memberId(store.getMember().getMemberId())
                .build();
    }
}
