package com.zerobase.reserve.domain.store.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class ListConverter implements AttributeConverter<List<String>, String> {
    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute.isEmpty()) {
            return null;
        }
        return String.join(DELIMITER, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData.isEmpty()) {
            return null;
        }
        return Arrays.stream(dbData.split(","))
                .toList();
    }
}
