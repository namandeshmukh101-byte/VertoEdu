package com.vertoedu;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import java.util.Base64;

public class TestJwtGen {

    @Test
    public void generateTestTokens() {
        String secret = "vertoedu_super_secret_key_for_development_2026_abcdefghijklmnopqrstuvwxyz123456";
        byte[] decodedKey = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(decodedKey);

        System.out.println("=================================================");
        printToken(key, "clashclasher1102@gmail.com", "TEACHER", 2L);
        printToken(key, "parthdeshmukh167@gmail.com", "TEACHER", 3L);
        printToken(key, "workgpt678@gmail.com", "PARENT", 4L);
        printToken(key, "regaltashwal@gmail.com", "PARENT", 5L);
        System.out.println("=================================================");
    }

    private void printToken(SecretKey key, String email, String role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println(email + "=" + token);
    }
}
