package com.zerobase.reserve.domain.store.repository;

import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.repository.projection.StoreProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {
    Page<Store> findAllByNameContains(String name, Pageable pageable);

    Optional<Store> findByStoreKey(String storeKey);

    boolean existsByStoreKey(String storeKey);

    @Query("select s.name as name, s.address.address as address, avg(r.score) as score " +
            "from Store s left join Review r on s.id = r.store.id " +
            "where ST_Distance_Sphere(ST_GeomFromText(:point, 4326), s.address.coordinate) <= 3000 " +
            "group by s.id")
    Slice<StoreProjection> findByDistance(@Param("point") String point,
                                          Pageable pageable);
}
