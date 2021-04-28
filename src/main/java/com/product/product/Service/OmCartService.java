package com.product.product.Service;

import com.product.product.Dto.OmCartDto;
import com.product.product.Entity.OmCart;
import reactor.core.publisher.Flux;

public interface OmCartService {
    public Flux<OmCart> callProductAPIInfo(OmCart omCart);
    public Flux<OmCartDto> getCartListAfterGroupby(String mbNo);
    public Flux<OmCartDto> getCartList(String mbNo);
    public Flux<OmCartDto> getCartListParrell(String mbNo);
}
