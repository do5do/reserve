package com.zerobase.reserve.domain.reservation.repository;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import com.zerobase.reserve.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface CustomReservationRepository {
    boolean existsReservation(Store store,
                              LocalDate reservationDate,
                              LocalTime reservationTime,
                              ReservationType reservationType);

    Page<Reservation> findReservationsFetchJoin(
            Store store, LocalDate reservationDate, Pageable pageable);

    Optional<Reservation> findReservation(Member member,
                                          Store store,
                                          LocalDate reservationDate,
                                          LocalTime reservationTime,
                                          ReservationType reservationType);
}
