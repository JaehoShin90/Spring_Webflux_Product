package com.product.product.security;


import com.product.product.Entity.Mmbr;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${jjwt.jjwt.secret}")
    private String secret;

    @Value("${jjwt.jjwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token){
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Mmbr mmbr){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", mmbr.getRoles());
        return doGenerateToken(claims, mmbr.getMmbrId());
    }

    private String doGenerateToken(Map<String, Object> claims, String username){
        Long expirationTimeLong = Long.parseLong(expirationTime);

        final Date createDate = new Date();
        final Date expirationDate = new Date(createDate.getTime() + expirationTimeLong * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token){
        return !isTokenExpired(token);
    }
}
