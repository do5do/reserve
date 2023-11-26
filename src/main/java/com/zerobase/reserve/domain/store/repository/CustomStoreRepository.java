package com.zerobase.reserve.domain.store.repository;

import com.zerobase.reserve.domain.store.entity.Store;

import java.util.Optional;

public interface CustomStoreRepository {
    Optional<Store> findByStoreKeyFetchJoin(String storeKey);

    void deleteByStoreKey(String storeKey);
}
