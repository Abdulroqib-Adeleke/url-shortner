package com.jug.url.auth;

import com.jug.url.dto.helper.TokenClaims;
import com.jug.url.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Value("${JWT_SECRET}")
    public String secret;

    public String generateToken(String email, Set<Roles> userRoles,String activeSessionId,UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",userRoles.stream().map(Enum::name).collect(Collectors.toSet()));
        claims.put("sessionId",activeSessionId);
        claims.put("tenancyId",String.valueOf(userId));
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("tenancyId");
    }


    public Date extractExpiration(String token) {

        return extractAllClaims(token).getExpiration();
    }

    public TokenClaims extractTokenClaims(String token){
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();
        String sessionId = (String)claims.get("sessionId");
        List<String> roles = claims.get("role",List.class);
        Set<Roles> rolesSet = roles.stream().map(Roles::valueOf).collect(Collectors.toSet());
        String tenancyId = (String)claims.get("tenancyId");

        return TokenClaims.builder()
                .userSessionId(sessionId)
                .roles(rolesSet)
                .username(username)
                .tenancyId(tenancyId)
                .build();
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
