package com.product.product.Service;

import com.product.product.Entity.Mmbr;
import reactor.core.publisher.Mono;

public interface MmbrService {
    public Mono<Mmbr> findByMmbrId(String mmbrId);
    public Mono<Long> checkAuthority(String mmbrId, String uri);
}
