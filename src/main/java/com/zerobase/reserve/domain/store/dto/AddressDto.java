package com.zerobase.reserve.domain.store.dto;

import com.zerobase.reserve.domain.store.entity.Address;
import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank
        String address,

        @NotBlank
        String detailAddr,

        @NotBlank
        String zipcode
) {
        public static Address toEntity(AddressDto addressDto) {
                return Address.builder()
                        .address(addressDto.address)
                        .detailAddr(addressDto.detailAddr)
                        .zipcode(addressDto.zipcode)
                        .build();
        }

        public static AddressDto fromEntity(Address addressEntity) {
                return new AddressDto(addressEntity.getAddress(),
                        addressEntity.getDetailAddr(),
                        addressEntity.getZipcode());
        }
}
