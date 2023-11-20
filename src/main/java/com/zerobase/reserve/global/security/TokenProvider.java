package com.zerobase.reserve.global.security;

import com.zerobase.reserve.domain.member.dto.model.MemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {
    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60L;
    private static final String KEY_ROLES = "roles";

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(MemberDto memberDto) {
        String authorities = memberDto.getRole().getKey();

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .subject(memberDto.getEmail())
                .claim(KEY_ROLES, authorities)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        return new UsernamePasswordAuthenticationToken(claims.getSubject(),
                token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get(KEY_ROLES).toString()));
    }

    public boolean validateToken(String token) {
        if (StringUtils.hasText(token)) {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        }

        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
