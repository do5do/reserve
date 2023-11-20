package com.zerobase.reserve.domain.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Builder
    public Address(String address, String detailAddr, String zipcode) {
        this.address = address;
        this.detailAddr = detailAddr;
        this.zipcode = zipcode;
    }

    public void addCoordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
