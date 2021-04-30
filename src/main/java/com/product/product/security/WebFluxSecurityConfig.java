package com.product.product.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebFluxSecurityConfig {
    private final BearerAuthenticationWebFilter authWebFilter;
    private final UriAuthorizationManager uriAuthorizationManager;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .exceptionHandling()
                .accessDeniedHandler((swe, e) ->
                        Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                )
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterAt(authWebFilter.getWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/login", "/member/signup").permitAll()
                .anyExchange().access(uriAuthorizationManager)
                .and()
                .build();


        //        return http.authorizeExchange()
//                .pathMatchers("/**").permitAll()
//                .pathMatchers("/admin/**").hasRole("ADMIN")
//                .and()
//                .httpBasic()
//                .and()
////                .addFilterAt(new MultipartCsrfFilter(), SecurityWebFiltersOrder.FIRST)
//                .csrf()
//                .disable()
////                .formLogin()
////                .loginPage("/LoginPage")
////                .authenticationSuccessHandler(loginSuccessHandler)
////                .authenticationFailureHandler(loginFailureHandler)
////                .and()
//                .logout().disable()
////                .authenticationManager(authenticationManager)
//                .exceptionHandling()
//                .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.BAD_REQUEST))
//                .and()
//                .build()
//                ;
    }
}
