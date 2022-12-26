package com.authserver.adapter.out.jwt;

import com.authserver.adapter.out.jwt.JwtToken.JwtTokenType;
import com.authserver.adapter.out.jwt.JwtVerifyResult.TokenStatus;
import com.authserver.application.port.out.jwt.JwtPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class JwtAdapter implements JwtPort {

    private final String SECRET_KEY;
    private final String ACCESS_EXPIRED_TIME;
    private final String REFRESH_EXPIRED_TIME;

    public JwtAdapter(
        @Value("${jwt.secret-key}") String SECRET_KEY,
        @Value("${jwt.access-expired-time}") String ACCESS_EXPIRED_TIME,
        @Value("${jwt.refresh-expired-time}") String REFRESH_EXPIRED_TIME) {
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_EXPIRED_TIME = ACCESS_EXPIRED_TIME;
        this.REFRESH_EXPIRED_TIME = REFRESH_EXPIRED_TIME;
    }

    @Override
    public JwtToken create(String username, JwtTokenType tokenType) {
        return switch (tokenType) {
            case ACCESS -> new JwtToken(username, createToken(username, ACCESS_EXPIRED_TIME),
                Long.parseLong(ACCESS_EXPIRED_TIME), JwtTokenType.ACCESS);
            case REFRESH -> new JwtToken(username, createToken(username, REFRESH_EXPIRED_TIME),
                Long.parseLong(ACCESS_EXPIRED_TIME), JwtTokenType.REFRESH);
            default -> throw new  AssertionError("지원하지 않는 토큰 타입!");
        };
    }

    private String createToken(String username, String time) {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(
                new Date(System.currentTimeMillis() + Long.parseLong(time)))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
    }

    @Override
    public JwtVerifyResult verify(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                .getBody();
            return new JwtVerifyResult(claims.getSubject(), TokenStatus.AVAILABLE);
        } catch (SecurityException e) {
            return new JwtVerifyResult(null, TokenStatus.DENIED);
        } catch (ExpiredJwtException e) {
            return new JwtVerifyResult(null, TokenStatus.EXPIRED);
        } catch (RuntimeException e) {
            return new JwtVerifyResult(null, TokenStatus.DENIED);
        }
    }
}
