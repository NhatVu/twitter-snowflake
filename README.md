## Twitter Snowflake implementation

### Requirement for generate ID system:
- IDs must be unique.
- IDs are numerical values only.
- IDs fit into 64-bit.
- IDs are ordered by date.
- Ability to generate over 10,000 unique IDs per second
- [Nice to have] Support distributed computing


### Snowflake idea: Split 64 bit into 5 parts:

[sign][timestamp][datacenterID][machineID][sequence number]
- sign: 1 bit
- timestamp: 41 bits  (important part that guarantee ID is sorted)
- datacenterID: 5 bits  (this part support distributed computing and be chosen at startup time. Should be fixed)
- machineID: 5 bits (this part support distributed computing and be chosen at startup time. Should be fixed)
- sequenceId: 12 bits  (support scenario where more IDs are generated in 1ms on the same machine)


In some system, people combine datacenterID + machineID, and called it workerId. In the case of Twitter, in 1 ms, we can generate 2 ^ (5 + 5 + 12) = 2 ^ 22 ~ 4 million IDs.

For timestamp, we need a startTime (called epochTime). Twitter choose epochTime = 1288834974657. Then, timestamp = currentTimeInMs - epochTime. So, when will timestamp be overflow? After 69 years = 2^41 ms / 1000 seconds / 365 days / 24 hours/ 3600 seconds

Sequence number: the number id generated in 1ms.

### SonyFlake

Ref: https://github.com/sony/sonyflake

- timestamp: 39 bits for time in units of 10 msec
- workerId: 16 bits for a machine id
- sequenceId: 8 bits for a sequence number

As a result, SonyFlake has the following advantages and disadvantages:

- The lifetime (174 years) is longer than that of Snowflake (69 years)
- It can work in more distributed machines (2^16) than Snowflake (2^10)
- It can generate 2^8 = 256 IDs per 10 msec at most in a single machine/thread (slower than Snowflake) ==> 25.6 ids / 1ms / 1 single machine ==> entire system, can generate 25.6 * 2^16 = 1.67 million id/ 1ms (compare to 4 million/1ms of Twitter design)


### Comments
Based on the requirement, we can change the number of bits for timestamp, workerId or sequenceId. Also, timestamp based on 10 ms, not 1 ms like Twitter.

This implement will try the way Sony do. 

Refs:
1. https://raw.githubusercontent.com/dromara/hutool/v5-master/hutool-core/src/main/java/cn/hutool/core/lang/Snowflake.java

