package com.product.product.config;

import com.product.product.Entity.Mmbr;
import com.product.product.Repository.MmbrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import reactor.core.publisher.Mono;

@Configuration
@EnableR2dbcAuditing
public class SpringSecurityAuditoAware implements ReactiveAuditorAware<Mmbr> {
    @Autowired
    private MmbrRepository mmbrRepository;

    @Override
    public Mono<Mmbr> getCurrentAuditor() {
        // 추후 Spring Security 연동 예정
        return mmbrRepository.findByMmbrId("savernet").log();
    }
}
