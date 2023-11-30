package com.example.snowflake.utils;

import java.awt.*;

public class SnowFlake {
    private long workerId;
    private long sequenceId;

    public long lastTimestamp;

    public SnowFlake(){
        this.workerId = SnowFlakeConstant.DEFAULT_WORKDER_ID;
    }

    /*
    need to be synchronized.
    for using lastTimestamp field, which is required to maintain state between multiple thread.
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < this.lastTimestamp) {
            if (this.lastTimestamp - timestamp < SnowFlakeConstant.BACKWARD_CLOCK) {
                // Tolerate specified backward/forward in time caused by NTP timing
                timestamp = lastTimestamp;
            } else {
                // If there is a problem with the server time (the clock goes back), an error will be reported.
                throw new IllegalStateException(String.format("Clock moved backwards. Refusing to generate id for %d ms", lastTimestamp - timestamp));
            }
        }

        if (timestamp - this.lastTimestamp <= SnowFlakeConstant.TIMESTAMP_UNIT) {
            // current timestamp is in range this.lastTimestamp + timestamp_unit
             long sequence = (this.sequenceId + 1);
            if (sequence >= SnowFlakeConstant.MAX_SEQUENCE_ID) {
                // overflow the sequenceId -> wait till next Timestamp
                sequence = 0L;
                timestamp = waitTillNextTimestamp(lastTimestamp);
            }
            this.sequenceId = sequence;
        } else {
            // go to new timestamp, reset sequenceId = 0
           sequenceId = 0L;
        }

        lastTimestamp = timestamp;
        long timeStampByUnit = (timestamp - SnowFlakeConstant.EPOCH_TIME) / SnowFlakeConstant.TIMESTAMP_UNIT;
        return (timeStampByUnit << SnowFlakeConstant.TIMESTAMP_SHIFT_LEFT)
                | (workerId << SnowFlakeConstant.WORKDER_ID_SHIFT_LEFT)
                | sequenceId;
    }

    private long waitTillNextTimestamp(long lastTimestamp){
        long curTime = System.currentTimeMillis();
        while(curTime - lastTimestamp <= SnowFlakeConstant.TIMESTAMP_UNIT){
            curTime = System.currentTimeMillis();
        }
        return curTime;
    }

    public long getTimeStamp(long id){
        return (id >> SnowFlakeConstant.TIMESTAMP_SHIFT_LEFT) & ((1L << SnowFlakeConstant.TIMESTAMP_SHIFT_LEFT) - 1L);
    }

    public long getWorkerId(long id){
        return (id >> SnowFlakeConstant.WORKDER_ID_SHIFT_LEFT) & ((1L << SnowFlakeConstant.WORKDER_ID_SHIFT_LEFT) - 1);
    }
}
