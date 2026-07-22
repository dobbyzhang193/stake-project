package com.hzhan.util;

import java.util.UUID;

public class SessionGenerator {
    public static String generateSessionKey() {
        return UUID.randomUUID().toString()
                .toUpperCase().replace("-", "").substring(0, 10);
    }
}
