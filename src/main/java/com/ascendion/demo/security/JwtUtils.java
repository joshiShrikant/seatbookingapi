package com.ascendion.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")   // in milliseconds
    private long jwtExpiration;

    // -------------------- Generate Token ---------------------
    public String generateToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(HashMap<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // -------------------- Extract Username ---------------------
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // -------------------- Validating Token ---------------------
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean isExpired = isTokenExpired(token);
        return (username.equals(userDetails.getUsername()) && !isExpired);
    }

    // -------------------- Extract Claims ---------------------
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // -------------------- Token Expiration ---------------------
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // -------------------- Secret Key ---------------------
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
