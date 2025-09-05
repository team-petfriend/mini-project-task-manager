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

/**
 2025-09-05 김승민
 JwtProvider 구현
**/

@Component
public class JwtProvider {

    /** Authorization 헤더(Bearer) */
    public static final String BEARER_PREFIX = "Bearer ";

    /** 커스텀 클래임 키(roles)*/
    public static final String CLAIM_ROLES = "roles";

    /** 서명용 비밀키, 엑세스 토큰 만료시간(ms)
     * application.properties에 
     * JWT에서 설정을 해준 값을 가져온다 */
    private final SecretKey key;
    private final long jwtExpirationMs;
    private final int clockSkewSeconds;
    
    /** 검증/파싱 파서를 생성자에서 1회 구성하여 재사용 */
    private final JwtParser parser;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpirationMs,
            @Value("${jwt.clock-skew-seconds:0}") int clockSkewSeconds
    ) {
        /** Decoders.BASE64.decode(secret)를 통해서 변환된 키값을 담아준다. */
        byte[] secretBytes = Decoders.BASE64.decode(secret);

        /** 32 bytes == 256 bits 길이 값을 검증*/
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("jwt.secret는 항상 256 비트 이상을 권장합니다.");
        }
        /** HMAC-SHA용 SecretKey 생성 */
        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.jwtExpirationMs = jwtExpirationMs;
        /** 최대값 최소값을 비교 음수를 방지 */
        this.clockSkewSeconds = Math.max(clockSkewSeconds, 0);
        this.parser = Jwts.parser()
                .verifyWith(this.key) // 검증용 키 등록
                .build(); // JwtParser 객체 생성
    }

    /** 엑세스 토큰을 생성
     * @Param username => subject에 저장할 사용자 식별
     * @Param roles => 권한 목록 (중복 제거를 위해 Set 사용) - JSON 배열
     * */
    public String generateJwtToken(String username, Set<String> roles) {
        /** 현재 시간을 밀리초(ms) 단위로 반환 */
        long now = System.currentTimeMillis();
        /** 현재 시간을 Date 객체로 반환*/
        Date iat = new Date(now);
        /** 현재 시간 + 토큰 유효기간(ms) JWT 만료시간으로 사용*/
        Date exp = new Date(now + jwtExpirationMs);

        /** <List> 타입으로 변환하여 직렬화
         *  true : roles의 값이 null 이라면 빈 배열을 반환
         *  false : roles의 값이 null이 아니라면 새로운 List에 roles를 담는다
         * */
        List<String> roleList = (roles == null) ? List.of() : new ArrayList<>(roles);
        
        
        /** generateJwtToken에 들어가는 실질적인 값을 생성해준다. */
        return Jwts.builder()
                .setSubject(username)       // 사용자 값
                .claim(CLAIM_ROLES, roles)  // 권한 목록
                .setIssuedAt(iat)           // 발행 시간
                .setExpiration(exp)         // 만료 시간
                .signWith(key)              // 설정한 비밀키
                .compact();                 // 빌더를 압축 JWT 문자열을 생성
    }

    /** HTTP Authorization 헤더에서 "Bearer"의 값을 처리해서 넘겨줘야한다. */
    public String removeBearer(String bearerToken) {
        /** bearerToken의 값이 없거나 "Bearer"를 포함하지 않는다면 예외처리*/
        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("Authorization Bearer token의 값이 존재하지않습니다. ");
        }
        
        /** "Bearer"를 포함한다면 BEARER_PREFIX 길이값 만큼 잘라내서 순수 토큰을 반환  */
        return bearerToken.substring(BEARER_PREFIX.length()).trim();
    }
    
    /** 내부 파싱(검증 포함) - 서명, 구조 검증한 뒤 Claims(페이로드)를 반환 */
    private Claims parseClaimsInternal(String token, boolean allowClockSkewOnExpiry) {
        try {
            /** 토큰 서명 검증 (key값으로 signature 확인)
             *  JWT 기본 구조 (헤더, 페이로드, 시그니처)
             *  성공 후 Claims 꺼내기 기능 .getPayload기능 사용가능
             *  .signWith(key) 서명, 구조 검증 -> 페이로드만 추출 후 반환
             * */
            return parser.parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException ex) {

            /** allowClockSkewOnExpiry 값이 true 이면서
             * clockSkewSeconds 0 초과이면서
             * getClaims의 값이 존재하는지 검증
             * */
            if (allowClockSkewOnExpiry && clockSkewSeconds > 0 && ex.getClaims() != null) {
                /** 만료시간을 담아준다 */
                Date exp = ex.getClaims().getExpiration();

                if (exp != null) {
                    /** 허용 오차를 밀리초로 변환해서 담아준다 */
                    long skewMs = clockSkewSeconds * 1000L;
                    /** 현재 시간을 담아준다 */
                    long now = System.currentTimeMillis();
                    /** 허용오차(now) - 만료 시각 <= 허용오차 범위 이내면 "방금 완료"로 간주*/
                    if (now - exp.getTime() <= skewMs) {
                        /** 설정값이 0보다 큰지 확인 */
                        return ex.getClaims(); // 예외에서 Claims를 꺼내 유효한 것으로 반환
                    }
                }
            }
            /** 허용 오차가 범위를 벗어나면 ExpiredJwtException ex 다시 호출 */
            throw ex;
        }   
    }
    
    /** 토큰 유효성 (서명/만료시간) 검증*/
    public boolean isValidToken(String tokenWithoutBearer) {
        try {
            parseClaimsInternal(tokenWithoutBearer, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Claims 추출 (검증 포함) */
    public Claims getClaims(String tokenWithoutBearer) {
        return parseClaimsInternal(tokenWithoutBearer, true);
    }


    /** 실제 페이로드 값 (Claims 값 추출) */
    public String getUsernameFromJwt(String tokenWithoutBearer) {return getClaims(tokenWithoutBearer).getSubject();}


    /** roles >> Set<String> 변환 */
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


    /** 남은 만료시간(ms)이 음수면 이미 만료 */
    public long getRemainingMillis(String tokenWithoutBearer) {
        Claims c = parseClaimsInternal(tokenWithoutBearer, true);
        return c.getExpiration().getTime() - System.currentTimeMillis();
    }
}
