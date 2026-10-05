package com.example.vinicius.service;

import com.example.vinicius.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generatedToken(User user){
         Key key= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

         return Jwts.builder()
                 .setIssuer("rede-api")
                 .subject(user.getEmail())
                 .issuedAt(new Date())
                 .expiration(Date.from(genExpirationDate()))
                 .signWith(key, SignatureAlgorithm.HS256)
                 .compact();
    }

    public String validateToken(String token){
        try{
            Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token).getPayload()
                    .getSubject();
        }catch(Exception e){
            return "";
        }
    }


    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
