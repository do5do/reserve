package com.zerobase.reserve.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER");

    private final String key;

    public static Role fromKey(String key) {
        return Arrays.stream(values()).filter(o -> o.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("권한 키가 존재하지 않습니다."));
    }
}
