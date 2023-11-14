package com.zerobase.reserve.domain.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddr;

    @Column(nullable = false)
    private String zipcode;
}
