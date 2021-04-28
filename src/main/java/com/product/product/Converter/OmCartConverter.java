package com.product.product.Converter;

import com.product.product.Entity.OmCart;
import com.product.product.Repository.OmCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OmCartConverter implements BeforeConvertCallback<OmCart> {

    @Autowired
    private OmCartRepository omCartRepository;

    @Override
    public Mono<OmCart> onBeforeConvert(OmCart entity, SqlIdentifier table) {
        if(entity.getCartSn() == null || entity.getCartSn().equals("")){
            return omCartRepository.getCartSnSeq()
                    .map(data -> {
                        entity.setCartSn(data);
                        return  entity;
                    });
        }else{
            return Mono.just(entity);
        }
    }
}
