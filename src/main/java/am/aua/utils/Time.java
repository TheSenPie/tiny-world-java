package am.aua.utils;

import java.time.Instant;

public class Time {
    public static final long NS_PER_SECOND = 1000000000;
    public static final long NS_PER_MS = 1000000;

    public static long NOW () {
        Instant instant = Instant.now();
        return instant.getEpochSecond() * NS_PER_SECOND + instant.getNano();
    }
}
