package com.keb.kebsmartfarm.jwt;


import com.keb.kebsmartfarm.dto.TokenDto;
import com.keb.kebsmartfarm.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final Integer REFRESH_TOKEN_EXPIRE_TIME;
    private final SecretKey key;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_token_exp}") long accessExp,
            @Value("${jwt.refresh_token_exp}") int refreshExp,
            RefreshTokenRepository refreshTokenRepository) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.ACCESS_TOKEN_EXPIRE_TIME = accessExp;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshExp;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // token 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        RefreshToken refreshToken = generateRefreshToken(authentication);
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .expiresIn(refreshToken.getExpiresIn())
                .build();
    }

    private String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .expiration(tokenExpiresIn)
                .signWith(key, SIG.HS512)
                .compact();
    }

    /*
     * Authentication은 Principal, Credentials, Authorities로 구성됨.
     * Principal에 저장된 유저아이디 꺼내서 토큰에 넣기
     */
    public RefreshToken generateRefreshToken(Authentication authentication) {
        Long userID = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userID, REFRESH_TOKEN_EXPIRE_TIME);
        return refreshTokenRepository.save(refreshToken);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                // Claims에 담겨있는 sub, auth, exp 하나 하나 SimpleGrantedAuthority로 변환
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // UserDetails.User(O), Entity.User(X)
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public TokenStatus validateAccessToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return TokenStatus.IS_VALID;
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException exception) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 서명입니다.");
            return TokenStatus.IS_EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 서명입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다");
        }
        return TokenStatus.IS_NOT_VALID;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
