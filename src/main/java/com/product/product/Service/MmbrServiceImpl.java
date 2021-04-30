package com.product.product.Service;

import com.product.product.Entity.Mmbr;
import com.product.product.Repository.MmbrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MmbrServiceImpl implements MmbrService{
    private final MmbrRepository mmbrRepository;

    @Override
    public Mono<Mmbr> findByMmbrId(String mmbrId) {
        return mmbrRepository.findByMmbrId(mmbrId);
    }

    @Override
    public Mono<Long> checkAuthority(String mmbrId, String uri) {
        return mmbrRepository.checkAuthority(mmbrId, uri);
    }
}
