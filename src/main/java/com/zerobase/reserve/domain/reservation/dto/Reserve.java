package com.zerobase.reserve.domain.reservation.dto;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.reservation.dto.model.ReservationDto;
import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import com.zerobase.reserve.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.format.annotation.DateTimeFormat.*;

public class Reserve {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String memberKey;

        @NotBlank
        private String storeKey;

        @NotBlank
        @DateTimeFormat(iso = ISO.DATE)
        private LocalDate reservationDate;

        @NotBlank
        @DateTimeFormat(pattern = "HH:mm:ss")
        private LocalTime reservationTime;

        public Reservation toEntity(String reservationKey, Member member, Store store) {
            Reservation reservation = Reservation.builder()
                    .reservationKey(reservationKey)
                    .reservationDate(reservationDate)
                    .reservationTime(reservationTime)
                    .reservationType(ReservationType.WAIT)
                    .build();

            reservation.addMemberAndStore(member, store);
            return reservation;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String storeName;
        private String storeKey;
        private LocalDate reservationDate;
        private LocalTime reservationTime;
        private Integer persons;
        private ReservationType reservationType;

        public static Response from(ReservationDto reservationDto) {
            return Response.builder()
                    .storeName(reservationDto.getStoreName())
                    .storeKey(reservationDto.getStoreKey())
                    .reservationDate(reservationDto.getReservationDate())
                    .reservationTime(reservationDto.getReservationTime())
                    .persons(reservationDto.getPersons())
                    .reservationType(reservationDto.getReservationType())
                    .build();
        }
    }
}
