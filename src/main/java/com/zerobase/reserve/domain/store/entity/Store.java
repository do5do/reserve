package com.zerobase.reserve.domain.store.entity;

import com.zerobase.reserve.domain.common.model.BaseTimeEntity;
import com.zerobase.reserve.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Store(String name, String description, Address address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
