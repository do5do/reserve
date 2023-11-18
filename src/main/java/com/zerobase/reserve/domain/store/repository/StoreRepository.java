package com.zerobase.reserve.domain.store.repository;

import com.zerobase.reserve.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllByNameContains(String name, Pageable pageable);

    Optional<Store> findByStoreId(String storeId);
}
