package com.zerobase.reserve.domain.reservation.repository;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import com.zerobase.reserve.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomReservationRepository {
    Optional<Reservation> findByReservationKey(String reservationKey);

    Optional<Reservation> findByMemberAndStoreAndReservationType(
            Member member, Store store, ReservationType reservationType);
}
