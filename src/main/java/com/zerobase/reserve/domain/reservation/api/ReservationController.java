package com.zerobase.reserve.domain.reservation.api;

import com.zerobase.reserve.domain.reservation.dto.Confirm;
import com.zerobase.reserve.domain.reservation.dto.ReservationsResponse;
import com.zerobase.reserve.domain.reservation.dto.Reserve;
import com.zerobase.reserve.domain.reservation.dto.Visit;
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

import static org.springframework.format.annotation.DateTimeFormat.ISO;

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

    /**
     * 점주용 예약 확인 (승인/취소)
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/confirm")
    public ResponseEntity<Confirm.Response> confirm(
            @RequestBody @Valid Confirm.Request request) {
        return ResponseEntity.ok(reservationService.confirm(request));
    }

    /**
     * 방문 확인
     */
    @PatchMapping("/visit")
    public ResponseEntity<?> visit(@RequestBody Visit.Request request) {
        return ResponseEntity.ok(reservationService.visit(request));
    }
}
