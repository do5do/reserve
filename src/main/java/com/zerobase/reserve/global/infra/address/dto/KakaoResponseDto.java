package com.zerobase.reserve.global.infra.address.dto;

import java.util.List;

public record KakaoResponseDto(List<Document> documents) implements CoordinateDto {
    public Document document() {
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
