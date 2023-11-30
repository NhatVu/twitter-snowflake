package com.example.snowflake.utils;

public class SnowFlakeConstant {
    public static final long TIMESTAMP_BITS = 39L;
    public static final long WORKDER_ID_BITS = 16L;
    public static final long SEQUENCE_ID_BITS = 8L;

    public static final long TIMESTAMP_UNIT = 10L; // 10ms

    public static final long MAX_SEQUENCE_ID = (1 << SEQUENCE_ID_BITS) - 1;

    public static final long WORKDER_ID_SHIFT_LEFT = SEQUENCE_ID_BITS;
    public static final long TIMESTAMP_SHIFT_LEFT = WORKDER_ID_BITS + SEQUENCE_ID_BITS;

    // default value
    public static final long DEFAULT_WORKDER_ID = 1L;
    public static final long EPOCH_TIME = 1701302400000L; //  30 November 2023 00:00:00 GMT 0

    /*
    The short answer is no, System.currentTimeMillis() is not monotonic. It is based on system time, and hence can be subject to variation either way (forward or backward) in the case of clock adjustments (e.g. via NTP).
    https://stackoverflow.com/questions/2978598/will-system-currenttimemillis-always-return-a-value-previous-calls
     */
    public static final long BACKWARD_CLOCK = 2000L; // ms

    public static void main(String[] args) {
        System.out.println(MAX_SEQUENCE_ID);
    }
}
