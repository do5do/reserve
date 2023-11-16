package com.zerobase.reserve.domain.common.util;

import java.util.UUID;

public class GenerateKey {
    public static String getUserKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
