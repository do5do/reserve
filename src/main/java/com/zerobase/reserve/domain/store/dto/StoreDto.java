package com.zerobase.reserve.domain.store.dto;

import com.zerobase.reserve.domain.store.entity.Store;

public record StoreDto(
        String name,
        String description,
        AddressDto addressDto

) {
    public static StoreDto fromEntity(Store store) {
        return new StoreDto(store.getName(), store.getDescription(),
                AddressDto.fromEntity(store.getAddress()));
    }
}
