package com.zerobase.reserve.domain.repository;

import com.zerobase.reserve.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
