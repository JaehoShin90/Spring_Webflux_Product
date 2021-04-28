package com.product.product.Handler;

import com.product.product.Entity.OmCart;
import com.product.product.Repository.OmCartRepository;
import com.product.product.Request.UserReq;
import com.product.product.Service.OmCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Slf4j
@Component
@RequiredArgsConstructor
public class MainHandler {
    @Autowired
    OmCartRepository omCartRepository;

    @Autowired
    OmCartService omCartService;

    public Mono<ServerResponse> setOmCartList(ServerRequest req){
        return req.bodyToFlux(OmCart.class)
                .flatMap(data ->
                    omCartService.callProductAPIInfo(data)
                            .flatMap(apiData -> {
                                    if(apiData.getReturnCode().equals("200")){
                                        return omCartRepository.save(apiData);
                                    }else{
                                        return Mono.just(-1);
                                    }
                            })
//                    return omCartRepository.save(data);
                )
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getOmcartCount(ServerRequest req){
        return omCartRepository.countByMbNo(req.queryParam("mbNo").get())
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    // 단건삭제
    public Mono<ServerResponse> delCartInfo(ServerRequest req){
        return req.bodyToMono(OmCart.class)
                .flatMap(data -> omCartRepository.deleteById(data.getCartSn()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    // 다중삭제
    public Mono<ServerResponse> delAllCartInfo(ServerRequest req){
        return omCartRepository.deleteAll(req.bodyToFlux(OmCart.class))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateOmCartOdQty(ServerRequest req){
        return req.bodyToMono(OmCart.class)
                .flatMap(data -> omCartRepository.save(data))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getCartListAfterGroupby(ServerRequest req){
        long startTime = System.currentTimeMillis();
        return omCartService.getCartListAfterGroupby(req.queryParam("mbNo").get()).collectList()
                .flatMap(data -> {
                    log.info("총 수행시간 : {}", System.currentTimeMillis() - startTime);
                    return ServerResponse.ok().bodyValue(data);
                });
    }

    /*
    * 10번 반복 결과
    * test : (최소, 최대, 평균) = (276, 587, 388,3)
    * test2 : (최소, 최대, 평균) = (264, 416, 353.2)
    * */
    public Mono<ServerResponse> getCartList(ServerRequest req){
        long startTime = System.currentTimeMillis();
        return omCartService.getCartList(req.queryParam("mbNo").get()).collectList()
                .flatMap(data -> {
                    log.info("총 수행시간 : {}", System.currentTimeMillis() - startTime);
                    return ServerResponse.ok().bodyValue(data);
                });
    }

    public Mono<ServerResponse> getCartListParallel(ServerRequest req){
        long startTime = System.currentTimeMillis();
        return omCartService.getCartListParrell(req.queryParam("mbNo").get()).collectList()
                .flatMap(data -> {
                    log.info("총 수행시간 : {}", System.currentTimeMillis() - startTime);
                    return ServerResponse.ok().bodyValue(data);
                });
    }

    public Mono<ServerResponse> test(ServerRequest req){
        long totalStartTime = System.currentTimeMillis();
        return Flux.range(1,50)
                .parallel()
                .runOn(Schedulers.newParallel("API call Test"))
                .concatMap(data -> {
                    long startTime = System.currentTimeMillis();

//                    return omCartService.getCartList("2")
                    return omCartService.getCartListParrell("2")
                            .collectList()
                            .flatMap(data2 -> {
                                log.info("총 수행시간 : {}", System.currentTimeMillis() - startTime);
                                return Mono.just(data2);
                            });
                })
                .sequential()
                .then(Mono.just("a"))
                .flatMap(data -> {
                        log.info("전체 수행 시간 : {}", System.currentTimeMillis() - totalStartTime);
                        return ServerResponse.ok().bodyValue("");
                });
    }
}
