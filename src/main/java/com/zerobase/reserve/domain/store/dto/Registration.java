package com.zerobase.reserve.domain.store.dto;

import com.zerobase.reserve.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Registration {
    @Getter
    public static class Request {
        @NotBlank
        private String memberId;

        @NotBlank
        private String storeName;

        @NotBlank
        private String description;

        @NotBlank
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
                message = "전화번호의 약식과 맞지 않습니다. ex) 02-123-1234")
        private String phoneNumber;

        private AddressDto address;

        private SalesInfoDto salesInfo;

        public Store toEntity(String storeId) {
            return Store.builder()
                    .storeId(storeId)
                    .name(storeName)
                    .description(description)
                    .phoneNumber(phoneNumber)
                    .address(AddressDto.toEntity(address))
                    .salesInfo(SalesInfoDto.toEntity(salesInfo))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String memberId;
        private String storeId;
        private String storeName;

        public static Response from(StoreDto storeDto) {
            return Response.builder()
                    .memberId(storeDto.getMemberId())
                    .storeId(storeDto.getStoreId())
                    .storeName(storeDto.getName())
                    .build();
        }
    }
}
