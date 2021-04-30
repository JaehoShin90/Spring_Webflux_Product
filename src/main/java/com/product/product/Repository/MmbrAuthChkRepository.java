package com.product.product.Repository;

import reactor.core.publisher.Mono;

public interface MmbrAuthChkRepository{
    public Mono<Long> checkAuthority(String userId, String uri);
}
