package com.zerobase.reserve.domain.common.util;

import java.util.UUID;

public class KeyGenerator {
    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
