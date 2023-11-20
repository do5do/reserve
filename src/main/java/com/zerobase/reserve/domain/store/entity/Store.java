package com.zerobase.reserve.domain.store.entity;

import com.zerobase.reserve.domain.common.model.BaseTimeEntity;
import com.zerobase.reserve.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(name = "idx_name", columnList = "name")})
@Entity
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String storeKey;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String phoneNumber;

    @Embedded
    private Address address;

    @Embedded
    private SalesInfo salesInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Store(String storeKey, String name, String description, String phoneNumber, Address address, SalesInfo salesInfo) {
        this.storeKey = storeKey;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.salesInfo = salesInfo;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
