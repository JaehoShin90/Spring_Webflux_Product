package com.product.product.security;

import org.springframework.stereotype.Component;

@Component
public class LoginFailureHandler {//implements ServerAuthenticationFailureHandler {

//    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
//
//    @Override
//    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
//        if(exception instanceof BadCredentialsException){
//            return redirectStrategy.sendRedirect(webFilterExchange.getExchange(), URI.create("LoginPage?err=" + exception.getMessage()));
//        }
//        return webFilterExchange.getChain().filter(webFilterExchange.getExchange());
//    }
}
