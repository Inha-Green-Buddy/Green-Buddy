package com.keb.kebsmartfarm.jwt;

import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refreshToken")
@Getter
@ToString
public class RefreshToken {
    @Id
    private String refreshToken;
    @Indexed
    private Long memberId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Integer expiresIn;

    public RefreshToken(final String refreshToken, final Long memberId, Integer expiresIn) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.expiresIn = expiresIn;
    }


}