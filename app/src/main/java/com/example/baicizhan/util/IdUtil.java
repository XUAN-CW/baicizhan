package com.example.baicizhan.util;

import java.util.UUID;

public class IdUtil {
    public static long generateLongId() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        return mostSignificantBits ^ leastSignificantBits;
    }
}
