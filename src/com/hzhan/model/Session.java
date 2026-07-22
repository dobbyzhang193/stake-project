package com.hzhan.model;

public class Session {
    private int customerId;
    private String key;
    private Long expireTime;

    public Session() {}

    public Session(int customerId, String key, Long expireTime) {
        this.customerId = customerId;
        this.key = key;
        this.expireTime = expireTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getOrCreate(int customerId) {
        return "";
    }

    public boolean isExpired() {
        return this.expireTime < System.currentTimeMillis();
    }

}
