package com.zerobase.reserve.domain.store.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.reserve.domain.store.repository.CustomStoreRepository;
import lombok.RequiredArgsConstructor;

import static com.zerobase.reserve.domain.store.entity.QStore.store;

@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteByStoreKey(String storeKey) {
        queryFactory
                .delete(store)
                .where(store.storeKey.eq(storeKey))
                .execute();
    }
}
