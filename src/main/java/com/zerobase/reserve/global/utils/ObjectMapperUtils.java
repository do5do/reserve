package com.zerobase.reserve.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reserve.global.exception.ApiBadRequestException;
import com.zerobase.reserve.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class ObjectMapperUtils {
    private final ObjectMapper objectMapper;

    public <T> T jsonToObject(String json, Class<T> tClass) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }

        try {
            objectMapper.configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException is occurred. ", e);
            throw new ApiBadRequestException(ErrorCode.JSON_TO_OBJECT_FAIL);
        }
    }
}
