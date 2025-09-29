package com.example.petfriend.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtProvider {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CLAIM_ROLES = "roles";
    private final SecretKey key;
    private final long jwtExpirationMs;
    private final int clockSkewSeconds;
    private final JwtParser parser;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpirationMs,
            @Value("${jwt.clock-skew-seconds:0}") int clockSkewSeconds
    ) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);

        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("jwt.secret는 항상 256 비트 이상을 권장합니다.");
        }
        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.jwtExpirationMs = jwtExpirationMs;
        this.clockSkewSeconds = Math.max(clockSkewSeconds, 0);
        this.parser = Jwts.parser()
                .verifyWith(this.key) // 검증용 키 등록
                .build(); // JwtParser 객체 생성
    }

    public String generateJwtToken(String username, Set<String> roles) {
        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + jwtExpirationMs);

        List<String> roleList = (roles == null) ? List.of() : new ArrayList<>(roles);

        return Jwts.builder()
                .setSubject(username)       // 사용자 값
                .claim(CLAIM_ROLES, roles)  // 권한 목록
                .setIssuedAt(iat)           // 발행 시간
                .setExpiration(exp)         // 만료 시간
                .signWith(key)              // 설정한 비밀키
                .compact();                 // 빌더를 압축 JWT 문자열을 생성
    }

    public String removeBearer(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("Authorization Bearer token의 값이 존재하지않습니다. ");
        }
        
        return bearerToken.substring(BEARER_PREFIX.length()).trim();
    }
    
    private Claims parseClaimsInternal(String token, boolean allowClockSkewOnExpiry) {
        try {
            return parser.parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException ex) {
            if (allowClockSkewOnExpiry && clockSkewSeconds > 0 && ex.getClaims() != null) {
                Date exp = ex.getClaims().getExpiration();

            if (exp != null) {
                long skewMs = clockSkewSeconds * 1000L;
                long now = System.currentTimeMillis();

                if (now - exp.getTime() <= skewMs) {
                    return ex.getClaims(); // 예외에서 Claims를 꺼내 유효한 것으로 반환
                    }
                }
            }
            throw ex;
        }   
    }
    
    public boolean isValidToken(String tokenWithoutBearer) {
        try {
            parseClaimsInternal(tokenWithoutBearer, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String tokenWithoutBearer) {
        return parseClaimsInternal(tokenWithoutBearer, true);
    }

    public String getUsernameFromJwt(String tokenWithoutBearer) {return getClaims(tokenWithoutBearer).getSubject();}

    public Set<String> getRolesFromJwt(String tokenWithoutBearer) {
        Object raw = getClaims(tokenWithoutBearer).get("roles");
        if (raw == null) return  Set.of();

        if (raw instanceof List<?> list) {
            Set<String> result = new HashSet<>();
            for (Object o : list) if (o != null) result.add(o.toString());
            return result;
        }

        if (raw instanceof Set<?> set) {
            Set<String> result = new HashSet<>();
            for (Object o : set) if (o != null) result.add(o.toString());
            return result;
        }
        return Set.of(raw.toString());
    }

    public long getRemainingMillis(String tokenWithoutBearer) {
        Claims c = parseClaimsInternal(tokenWithoutBearer, true);
        return c.getExpiration().getTime() - System.currentTimeMillis();
    }
}
