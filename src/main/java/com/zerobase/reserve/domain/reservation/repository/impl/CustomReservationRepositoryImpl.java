package com.zerobase.reserve.domain.reservation.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.repository.CustomReservationRepository;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import com.zerobase.reserve.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.zerobase.reserve.domain.member.entity.QMember.member;
import static com.zerobase.reserve.domain.reservation.entity.QReservation.reservation;

@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsReservation(Store store,
                                     LocalDate reservationDate,
                                     LocalTime reservationTime,
                                     ReservationType reservationType) {
        Reservation fetchOne = queryFactory
                .selectFrom(reservation)
                .where(
                        reservation.store.eq(store),
                        reservation.reservationDate.eq(reservationDate),
                        reservation.reservationTime.eq(reservationTime),
                        reservation.reservationType.eq(reservationType)
                )
                .fetchOne();

        return fetchOne != null;
    }

    @Override
    public Page<Reservation> findReservationsFetchJoin(
            Store store, LocalDate reservationDate, Pageable pageable) {
        List<Reservation> contents = queryFactory
                .selectFrom(reservation)
                .join(reservation.member, member)
                .fetchJoin()
                .where(
                        reservation.store.eq(store),
                        reservation.reservationDate.eq(reservationDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(
                        reservation.store.eq(store),
                        reservation.reservationDate.eq(reservationDate)
                );

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }
}
