package com.product.product.security;

import com.product.product.Service.MmbrService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UriAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Autowired
    MmbrService mmbrService;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        ServerHttpRequest req = object.getExchange().getRequest();
        return authentication.flatMap(auth -> {
            if(Objects.nonNull(auth.getPrincipal()) == false)
                return Mono.just(new AuthorizationDecision(false));

            Mono<Long> authCount = mmbrService.checkAuthority(auth.getPrincipal().toString(), req.getURI().getPath());
            return authCount.flatMap(count -> {
                boolean isAuth = count > 0L ? true : false;
                return Mono.just(new AuthorizationDecision(isAuth));
            });
//            return Mono.just(new AuthorizationDecision(true));
        });
    }
}
