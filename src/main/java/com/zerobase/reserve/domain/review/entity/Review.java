package com.zerobase.reserve.domain.review.entity;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.review.dto.Update;
import com.zerobase.reserve.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contents;

    @ColumnDefault("0")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Review(String contents, Integer score) {
        this.contents = contents;
        this.score = score;
    }

    public void addMemberAndStore(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public void updateReview(Update.Request request) {
        this.contents = request.getContents();
        this.score = request.getScore();
    }
 }
