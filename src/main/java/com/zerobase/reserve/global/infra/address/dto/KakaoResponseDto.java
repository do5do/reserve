package com.zerobase.reserve.global.infra.address.dto;

import com.zerobase.reserve.global.exception.ApiBadRequestException;
import com.zerobase.reserve.global.exception.ErrorCode;

import java.util.List;

public record KakaoResponseDto(List<Document> documents) implements CoordinateDto {
    public Document document() {
        if (documents.isEmpty()) {
            throw new ApiBadRequestException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return documents.get(0);
    }

    @Override
    public Double getX() {
        return document().x;
    }

    @Override
    public Double getY() {
        return document().y;
    }

    public record Document(
            Double x,
            Double y
    ) {}
}
