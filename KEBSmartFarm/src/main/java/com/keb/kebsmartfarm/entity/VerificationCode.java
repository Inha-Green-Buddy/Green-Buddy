package com.keb.kebsmartfarm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "verify", timeToLive = 300L)
public class VerificationCode {
    @Id
    private String userEmail;
    private String code;

    public VerificationCode(String userEmail, String code) {
        this.userEmail = userEmail;
        this.code = code;
    }

    public boolean matches(String code) {
        return this.code.equals(code);
    }
}
