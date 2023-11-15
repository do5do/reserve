package com.zerobase.reserve.domain.reservation.entity;

import com.zerobase.reserve.domain.common.model.BaseTimeEntity;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.reservation.type.ApprovalType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime reservedDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;

    @Column(nullable = false)
    private boolean arrival = false; // 매장 도착 확인 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void setMember(Member member) {
        this.member = member;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
