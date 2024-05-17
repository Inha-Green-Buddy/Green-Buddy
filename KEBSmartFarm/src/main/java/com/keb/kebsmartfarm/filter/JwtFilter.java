package com.keb.kebsmartfarm.filter;

import com.keb.kebsmartfarm.config.JsonUtil;
import com.keb.kebsmartfarm.jwt.TokenProvider;
import com.keb.kebsmartfarm.jwt.TokenStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(BEARER_PREFIX)[1];
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt)) {
            TokenStatus status = tokenProvider.validateToken(jwt);
            if (status == TokenStatus.IS_VALID) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (status == TokenStatus.IS_EXPIRED) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, Boolean> error = new HashMap<>();
                error.put("token_expired", true);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JsonUtil.toJson(error));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
