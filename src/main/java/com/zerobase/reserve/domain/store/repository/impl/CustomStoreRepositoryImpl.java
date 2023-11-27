package com.zerobase.reserve.domain.store.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.repository.CustomStoreRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.zerobase.reserve.domain.member.entity.QMember.member;
import static com.zerobase.reserve.domain.store.entity.QStore.store;

@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Store> findByStoreKeyFetchJoin(String storeKey) {
        return Optional.ofNullable(queryFactory
                .selectFrom(store)
                .join(store.member, member).fetchJoin()
                .where(store.storeKey.eq(storeKey))
                .fetchOne());
    }

    @Override
    public void deleteByStoreKey(String storeKey) {
        queryFactory
                .delete(store)
                .where(store.storeKey.eq(storeKey))
                .execute();
    }
}
