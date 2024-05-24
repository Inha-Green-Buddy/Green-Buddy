package com.keb.kebsmartfarm.entity;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "verify", timeToLive = 300L)
public class VerificationCode {
    @Id
    private final String userEmail;
    private final String code;

    private VerificationCode(String userEmail, String code) {
        this.userEmail = userEmail;
        this.code = code;
    }

    public boolean matches(String code) {
        return this.code.equals(code);
    }

    public static VerificationCode fromUserEmail(String userEmail) {
        String code = UUID.randomUUID().toString().substring(0, 6);
        return new VerificationCode(userEmail, code);
    }

    @Override
    public String toString() {
        return code;
    }
}
