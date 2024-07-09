package com.goormcoder.ieum.jwt;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.dto.response.JwtTokenDto;
import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;

    @Value("${jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenDto generateToken(Member member) {
        return new JwtTokenDto(
                generateAccessToken(member),
                generateRefreshToken(member)
        );
    }

    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date accessTokenExpiresIn = new Date(now.getTime() + accessTokenExpirationMs);
        return Jwts.builder()
                .claim("id", member.getId())
                .claim("role", member.getRole().getValue())
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Member member) {
        Date now = new Date();
        Date refreshTokenExpiresIn = new Date(now.getTime() + refreshTokenExpirationMs);
        return Jwts.builder()
                .claim("id", member.getId())
                .claim("role", member.getRole().getValue())
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        if (claims.get("id") == null || claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        String id = claims.get("id").toString();
        String role = claims.get("role").toString();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        return new UsernamePasswordAuthenticationToken(new UserPrincipal(id), "", Collections.singletonList(authority));
    }

}
