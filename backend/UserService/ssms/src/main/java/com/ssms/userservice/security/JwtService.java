package com.ssms.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Use ONE method for both signing and verifying
    private Key getSigningKey() {
        // UTF_8 is the standard for plain-text secrets
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userName, String role,Long companyId,String country, String currencyCode) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",role);
        claims.put("companyId", companyId);
        claims.put("country", country);          // Added regional claim
        claims.put("currencyCode", currencyCode);  // Added currency claim
        String token = createToken(claims, userName);
        System.out.println("[JWT] Generated token for user: " + userName + " with role: " + role+ "companyId: "+ companyId);
        return token;
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Matches validation
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Matches generation
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(final String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Matches generation
                .build()
                .parseClaimsJws(token);
    }
}