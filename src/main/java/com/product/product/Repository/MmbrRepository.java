package com.product.product.Repository;

import com.product.product.Entity.Mmbr;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MmbrRepository extends ReactiveCrudRepository<Mmbr, String>, MmbrAuthChkRepository {
    Mono<Mmbr> findByMmbrId(String mmbrId);
}
