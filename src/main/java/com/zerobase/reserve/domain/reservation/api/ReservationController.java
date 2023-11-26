package com.zerobase.reserve.domain.reservation.api;

import com.zerobase.reserve.domain.reservation.dto.ReservationsResponse;
import com.zerobase.reserve.domain.reservation.dto.Reserve;
import com.zerobase.reserve.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.*;

@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    /**
     * 예약
     */
    @PostMapping
    public ResponseEntity<Reserve.Response> reserve(
            @RequestBody @Valid Reserve.Request request) {
        return ResponseEntity.ok(
                Reserve.Response.from(reservationService.reserve(request)));
    }

    /**
     * 점주용 날짜별 예약 조회
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<Page<ReservationsResponse>> reservations(
            @RequestParam @NotBlank String storeKey,
            @RequestParam @NotNull @DateTimeFormat(iso = ISO.DATE)
            LocalDate reservationDate,
            final Pageable pageable) {
        return ResponseEntity.ok(reservationService.reservations(
                storeKey, reservationDate, pageable));
    }

    // 방문 확인? 회원의 확정된(예약 타입이 confirm) 예약 정보 조회를 한 뒤 -> 회원 번호로 조회
    // 현재 시간이 예약 시간 10분 전인 경우 도착 확인을 한다. (arrival true로 변경)

}
