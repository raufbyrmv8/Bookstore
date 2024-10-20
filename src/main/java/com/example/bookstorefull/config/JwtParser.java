package com.example.bookstorefull.config;

import com.example.bookstorefull.model.Authority;
import com.example.bookstorefull.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtParser {
    private Key key;
    // add to secret !
    private String secret = "bWljcm9zZXJ2aWNlMjUgbWljcm9zZXJ2aWNlMjUgbWljcm9zZXJ2aWNlMjUg";
    private byte[]bytes;


    @PostConstruct
    public void init(){
        bytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getName())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(5))))
                .signWith(key, SignatureAlgorithm.HS256)
                .claim("authority", user.getAuthorities().stream().map(Authority::getUserAuthority).collect(Collectors.toList()))
                .compact();
    }
}
