package com.zerobase.reserve.domain.reservation.dto.model;

import com.zerobase.reserve.domain.reservation.type.ReservationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private String reservationKey;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Integer persons;
    private ReservationType reservationType;
    private boolean arrival;
    private String storeName;
    private String storeKey;

    public static ReservationDto fromEntity(com.zerobase.reserve.domain.reservation.entity.Reservation reservation) {
        return ReservationDto.builder()
                .reservationKey(reservation.getReservationKey())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .persons(reservation.getPersons())
                .reservationType(reservation.getReservationType())
                .arrival(reservation.isArrival())
                .storeName(reservation.getStore().getName())
                .storeKey(reservation.getStore().getStoreKey())
                .build();
    }
}
