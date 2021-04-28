package com.product.product.Repository;

import com.product.product.Entity.Mmbr;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MmbrRepository extends ReactiveCrudRepository<Mmbr, String> {
    Mono<Mmbr> findByMmbrId(String mmbrId);
}
