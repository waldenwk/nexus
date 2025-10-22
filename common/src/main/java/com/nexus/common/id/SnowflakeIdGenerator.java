package com.nexus.common.id;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Snowflake ID生成器
 * 生成全局唯一的64位ID
 */
@Component
public class SnowflakeIdGenerator {
    
    // 时间戳位数
    private final long timestampBits = 41L;
    // 数据中心ID位数
    private final long datacenterIdBits = 5L;
    // 机器ID位数
    private final long workerIdBits = 5L;
    // 序列号位数
    private final long sequenceBits = 12L;
    
    // 最大数据中心ID
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 最大机器ID
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 最大序列号
    private final long maxSequence = -1L ^ (-1L << sequenceBits);
    
    // 时间戳左移位数
    private final long timestampShift = sequenceBits + workerIdBits + datacenterIdBits;
    // 数据中心ID左移位数
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    // 机器ID左移位数
    private final long workerIdShift = sequenceBits;
    
    // 起始时间戳 (2022-01-01)
    private final long epoch = 1640995200000L;
    
    // 数据中心ID
    private final long datacenterId;
    // 机器ID
    private final long workerId;
    // 序列号
    private long sequence = 0L;
    // 上一次时间戳
    private long lastTimestamp = -1L;
    
    public SnowflakeIdGenerator() {
        this.datacenterId = getDatacenterId();
        this.workerId = getWorkerId();
    }
    
    /**
     * 生成下一个ID
     * @return 唯一ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        
        // 如果当前时间小于上一次ID生成时间，说明系统时钟回退过，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟向后移动，拒绝生成ID " + (lastTimestamp - timestamp) + " 毫秒");
        }
        
        // 如果是同一毫秒内生成的，则进行序列号递增
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            // 如果序列号溢出，等待下一毫秒
            if (sequence == 0) {
                timestamp = waitNextMillis(timestamp);
            }
        } else {
            // 如果不是同一毫秒，序列号重置
            sequence = 0L;
        }
        
        lastTimestamp = timestamp;
        
        // 组装ID
        return ((timestamp - epoch) << timestampShift) |
               (datacenterId << datacenterIdShift) |
               (workerId << workerIdShift) |
               sequence;
    }
    
    /**
     * 等待下一毫秒
     * @param lastTimestamp 上一次时间戳
     * @return 下一毫秒时间戳
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
    
    /**
     * 获取数据中心ID
     * @return 数据中心ID
     */
    private long getDatacenterId() {
        long id = 1L;
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String[] parts = hostAddress.split("\\.");
            if (parts.length >= 4) {
                id = (Long.parseLong(parts[3]) % 32) & maxDatacenterId;
            }
        } catch (UnknownHostException e) {
            // 忽略异常，使用默认值
        }
        return id;
    }
    
    /**
     * 获取机器ID
     * @return 机器ID
     */
    private long getWorkerId() {
        long id = 1L;
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            // 使用主机名的哈希值作为机器ID
            id = Math.abs(hostName.hashCode() % 32) & maxWorkerId;
        } catch (UnknownHostException e) {
            // 忽略异常，使用默认值
        }
        return id;
    }
}