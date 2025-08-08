package com.alloymobile.restapi.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.security.Key;

@Component
public class JwtUtil {
    private static final String SECRET = "werewrewrewrewrewrewrewwerewrewwreewrWREWREWREWREWREWREWRWEREWREWREWRWEREWREWREWREWRWEREWREWREWREWREW";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    Integer expirationInMinutes = 15;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (expirationInMinutes * 60 * 1000)))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public Date extractTokenExpiry(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // Token is expired
        } catch (JwtException e) {
            return true; // Token is invalid
        }
    }

    public void setJwtInCookie(HttpServletResponse response, String token) {
        String sameSiteHeader = String.format(
                "authToken=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=None",
                token, expirationInMinutes * 60);
        response.setHeader("Set-Cookie", sameSiteHeader);
    }

    public String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void clearJwtCookie(HttpServletResponse response) {
        String expiredCookie = "authToken=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=None";
        response.setHeader("Set-Cookie", expiredCookie);
    }

    public Integer getTimeoutSeconds() {
        return expirationInMinutes * 60; // Convert minutes to seconds
    }
}