package com.zerobase.reserve.domain.store.repository;

import com.zerobase.reserve.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {
    Page<Store> findAllByNameContains(String name, Pageable pageable);

    Optional<Store> findByStoreKey(String storeKey);

    boolean existsByStoreKey(String storeKey);
}
