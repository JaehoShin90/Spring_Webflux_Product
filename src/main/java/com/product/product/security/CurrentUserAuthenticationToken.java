package com.product.product.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CurrentUserAuthenticationToken {
    private final JWTUtil jwtUtil;

    public CurrentUserAuthenticationToken(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Mono<Authentication> create(String authToken){
        if(jwtUtil.validateToken(authToken) == false){
            return Mono.empty();
        }
        Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
        List<String> rolesMap = claims.get("role", List.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(Objects.nonNull(rolesMap)){
            rolesMap.stream().forEach(rolemap -> {
                authorities.add(new SimpleGrantedAuthority(rolemap));
            });
        }
        return Mono.just(new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities));
    }
}
