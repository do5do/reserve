package com.zerobase.reserve.domain.store.dto;

import com.zerobase.reserve.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;

public record Registration(
        @NotBlank
        String memberId,

        @NotBlank
        String storeName,

        @NotBlank
        String description,

        AddressDto addressDto
) {
        public Store toEntity() {
                return Store.builder()
                        .name(storeName)
                        .description(description)
                        .address(AddressDto.toEntity(addressDto))
                        .build();
        }
}
