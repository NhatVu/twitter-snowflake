package com.example.snowflake;

import com.example.snowflake.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class Test {
    public static void main(String[] args) {

        SnowFlake snowFlake = new SnowFlake();
        long id = snowFlake.nextId();
        System.out.println(id);
        System.out.println("timestamp unit test: " + snowFlake.getTimeStamp(id));
        System.out.println("workerId: " + snowFlake.getWorkerId(id));
        Set<Long> set = new HashSet<>();
        int count = 0;
        while (true){
            id = snowFlake.nextId();
            count++;
            if(set.contains(id)){
                System.out.println("duplicated");
                break;
            }
            set.add(id);
            if(count % 1000 == 0){
                log.info(count + "");
            }
        }
    }
}
