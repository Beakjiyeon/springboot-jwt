package com.example.jwtlogin.security;

import com.example.jwtlogin.dto.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    // 토큰 유효 시간
    public final static long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 2; // 2분
    public final static long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 10; // 2분


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); // 해싱 알고리즘
    }

    /**
     * 유저 정보로 액세스 토큰, 리프레시 토큰 발급
     * @param authentication
     * @return
     */
    public TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        // debug authentication.getAuthorities()
        Collection<? extends GrantedAuthority> debugAuthorities = authentication.getAuthorities();
        log.debug("### debug authorities: " + debugAuthorities);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(Jwts.builder()
                        .setSubject(authentication.getName())
                        .claim("auth", authorities)
                        .setExpiration(new Date(now + ACCESS_TOKEN_VALID_TIME)) // Date 타입 지원
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact())
                .refreshToken(Jwts.builder()
                        .setExpiration(new Date(now + REFRESH_TOKEN_VALID_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact())
                .build();
    }

    /**
     * 토큰을 복호화하여 정보 조회
     * @param accessToken
     * @return
     */
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 생성하여 authentication 객체에 반영한다.
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰 복호화
     * @param accessToken
     * @return
     */
    public Claims parseClaims(String accessToken) {
        try {
            // 비밀키로 토큰을 복호화
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 토큰 검증
     * @param toekn
     * @return
     */
    public boolean validateToken(String toekn) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(toekn);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }




}
