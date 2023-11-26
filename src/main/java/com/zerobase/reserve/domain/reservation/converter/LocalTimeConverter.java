package com.zerobase.reserve.domain.reservation.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalTime;
import java.util.Arrays;

@Converter
public class LocalTimeConverter implements AttributeConverter<LocalTime, String> {
    @Override
    public String convertToDatabaseColumn(LocalTime attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.toString();
    }

    @Override
    public LocalTime convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        int[] data = Arrays.stream(dbData.split(":"))
                .mapToInt(Integer::parseInt)
                .toArray();
        return LocalTime.of(data[0], data[1]);
    }
}
