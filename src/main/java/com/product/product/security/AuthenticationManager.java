package com.product.product.security;

import com.product.product.Repository.MmbrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private MmbrRepository mmbrRepository;

//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        String mmbrId = authentication.getPrincipal().toString();
//        String mmbrPwd = authentication.getCredentials().toString();
//        return mmbrRepository.findByMmbrId(mmbrId)
//                .switchIfEmpty(Mono.error(new BadCredentialsException("없어")))
//                .flatMap(loginData -> {
//                    if(loginData.getPwdChk(mmbrPwd)){
//                        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(mmbrId, mmbrPwd, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//                        result.setDetails(loginData);
//                        return Mono.just(result);
//                    }else{
//                        throw new BadCredentialsException("비밀번호틀려");
//                    }
//                });
//    }
    @Override
    public Mono<Authentication> authenticate(Authentication authentication){
        return Mono.just(authentication);
    }
}
