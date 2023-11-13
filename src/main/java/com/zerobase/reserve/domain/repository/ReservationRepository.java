package com.zerobase.reserve.domain.repository;

import com.zerobase.reserve.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
