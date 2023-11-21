package com.zerobase.reserve.domain.common.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KeyGenerator {
    public String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}