package com.zerobase.reserve.domain.reservation.dto;

import com.zerobase.reserve.domain.common.validator.ValidEnum;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import jakarta.validation.constraints.NotBlank;

public class Confirm {
    public record Request(
            @NotBlank
            String reservationKey,

            @ValidEnum(enumClass = ReservationType.class)
            ReservationType reservationType
    ) {
    }

    public record Response(
            String reservationKey
    ) {}
}
