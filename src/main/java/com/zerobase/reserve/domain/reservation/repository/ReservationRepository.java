package com.zerobase.reserve.domain.reservation.repository;

import com.zerobase.reserve.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
