package com.product.product.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Component
public class BearerAuthenticationWebFilter {
    private final AuthenticationManager authenticationManager;
    private final CurrentUserAuthenticationToken currentUserAuthenticationToken;

    private static final String BEARER = "Bearer ";
    private static final Predicate<String> matchBearereLength = authValue -> authValue.length() > BEARER.length();
    private static final Function<String, Mono<String>> isolateBearereValues = authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length()));

    public WebFilter getWebFilter(){
        AuthenticationWebFilter authWebFilter = new AuthenticationWebFilter(authenticationManager);
        authWebFilter.setServerAuthenticationConverter((serverWebExchange) ->
            Mono.justOrEmpty(serverWebExchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION)
            ).filter(matchBearereLength)
                    .flatMap(isolateBearereValues)
                    .flatMap(currentUserAuthenticationToken::create)
        );

        return authWebFilter;
    }
}
