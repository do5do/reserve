package com.zerobase.reserve.domain.store.dto;

import com.zerobase.reserve.domain.store.entity.SalesInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

public record SalesInfoDto(
        @DateTimeFormat(pattern = "HH:mm:ss")
        LocalTime operatingStart,

        @DateTimeFormat(pattern = "HH:mm:ss")
        LocalTime operatingEnd,

        List<String> closedDays
) {

    public static SalesInfo toEntity(SalesInfoDto salesInfoDto) {
        return SalesInfo.builder()
                .operatingStart(salesInfoDto.operatingStart)
                .operatingEnd(salesInfoDto.operatingEnd)
                .closedDays(salesInfoDto.closedDays)
                .build();
    }

    public static SalesInfoDto fromEntity(SalesInfo salesInfo) {
        return new SalesInfoDto(salesInfo.getOperatingStart(),
                salesInfo.getOperatingEnd(), salesInfo.getClosedDays());
    }
}
