package com.product.product.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginSuccessHandler {// implements ServerAuthenticationSuccessHandler {
//
//    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
//
//    @Override
//    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
//        log.info("로그인 성공");
//        return redirectStrategy.sendRedirect(webFilterExchange.getExchange(), URI.create("PrdtCmpn?suc=SS"));
////        return null;
//
//    }
}
