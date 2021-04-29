package com.product.product.Service;

import com.product.product.Dto.OmCartDto;
import com.product.product.Dto.ProductDtlDto;
import com.product.product.Entity.OmCart;
import com.product.product.Repository.OmCartRepository;
import com.product.product.Request.CartGroupReq;
import com.product.product.Util.Common;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OmCartServiceImpl implements OmCartService{

    @Autowired
    private OmCartRepository omCartRepository;

    @Autowired
    private Common cmmn;

    private Flux<OmCart> callProductAPI(CartGroupReq cartGroupReq){
        return cmmn.callAPIPublisher(
                        "/product/v1/detail/productDetailList?dataType=LIGHT2"
                        , Flux.just(cartGroupReq)
                        , CartGroupReq.class
                )
                .bodyToFlux(ProductDtlDto.class)
                .map(data -> data.getData()[0])
                ;
    }

    public Flux<OmCart> callProductAPIInfo(OmCart omCart){
        return cmmn.callAPIPublisher(
                        "/product/v1/detail/productDetailList?dataType=LIGHT2"
                        , Flux.just(omCart)
                        , CartGroupReq.class
                )
                .bodyToFlux(ProductDtlDto.class)
                .map(data -> data.getData()[0])
                .map(data -> cmmn.setCopyData(omCart, data));
    }

    public Flux<OmCartDto> getCartListAfterGroupby(String mbNo){
        return omCartRepository.findByMbNoOrderByModDttmDesc(mbNo)
                // n개의 Flux 생성(spdNo와 sitmNo로 그룹핑)
                // groupBy를 쓰는 객체에 @Data 어노테이션이 없으면 그룹핑이 안된다.(equals와 hashCode로 비교를 하는 것으로 추정)
                .groupBy(omCart ->
                        new CartGroupReq(omCart.getSpdNo(), omCart.getSitmNo(), omCart.getTrNo()) // 그룹핑 조건(상품)
                )
                .concatMap(groups ->  // concatMap을 쓸 경우 순서 보장 대신 속도는 flatMap보다는 느릴 수 있음
                        {
                            log.info("Groups : {}",groups.key());
                            return callProductAPI(groups.key()).flatMap(apiData -> {
                                log.info("apiData : {}", apiData);
                                // 만약 groups.flatMap에서 api 호출을 하게 되면 기존의 장바구니 상품 수 만큼 호출을 하게됨
                                return groups.map(orgData -> {
                                    log.info("orgData : {}", orgData);
                                    return cmmn.setCopyData(orgData, apiData); // 원본 데이터에 조회한 값 반영
                                });
                            });
                        }
                )
                .groupBy(
                        omCart -> new OmCartDto(omCart.getMbNo(), omCart.getTrNo()) // 그룹핑 조건(업체)
                )
                .concatMap(cartDtoGroups ->{
                    return Flux.just(cartDtoGroups.key())
                            .zipWith(cartDtoGroups.collectList())
                            .map(ass -> ass.getT1().withCartList(ass.getT2())); // 원본 데이터에 그룹핑한 값 반영
                })
                ;
    }

    public Flux<OmCartDto> getCartList(String mbNo){
        Flux<OmCart> omCartList = omCartRepository.findByMbNoOrderByModDttmDesc(mbNo).cache(Duration.ofSeconds(10));
        return cmmn.callAPIPublisher(
                    "/product/v1/detail/productDetailList?dataType=LIGHT2"
                    , omCartList.groupBy(omCart -> new CartGroupReq(omCart.getSpdNo(), omCart.getSitmNo(), omCart.getTrNo()))
                            .map(groups -> groups.key())
                            .collectList()
                    , CartGroupReq.class
                )
                .bodyToFlux(ProductDtlDto.class)
                .map(data ->  data.getData())
                .concatMap(apiDataList ->
                        omCartList.concatMap(omCart -> {
//                            log.info("omCartList data : {}", omCart);
                            for (OmCart apiData : apiDataList) {
                                if (apiData.isSameProduct(omCart)) {
                                    cmmn.setCopyData(omCart, apiData);
                                    break;
                                }
                            }
                            return Flux.just(omCart);
                        })
                ).groupBy(
                        omCart -> new OmCartDto(omCart.getMbNo(), omCart.getTrNo()) // 그룹핑 조건(업체)
                )
                .concatMap(cartDtoGroups ->
                        Flux.just(cartDtoGroups.key())
                                .zipWith(cartDtoGroups.collectList())
                                .map(ass -> ass.getT1().withCartList(ass.getT2())) // 원본 데이터에 그룹핑한 값 반영
                )
                ;
    }

    public Flux<OmCartDto> getCartListParrell(String mbNo){
        Flux<OmCart> omCartList = omCartRepository.findByMbNoOrderByModDttmDesc(mbNo).cache(Duration.ofSeconds(10));
        return this.setAPIParamList(omCartList)
                .map(this::setAPIParamLimit)
                .flatMapMany(data -> data)
                .parallel()
                .runOn(Schedulers.newParallel("Api-Parallel"))
                .concatMap(group -> this.parallelAPITest(group, omCartList))
                .sequential()
                .thenMany(omCartList)
//                .then(Mono.just(omCartList))
//                .flatMapMany(data -> data)
                .groupBy(omCart ->  {
//                    log.info("After Sequential : {}", omCart.getCartSn());
                    return new OmCartDto(omCart.getMbNo(), omCart.getTrNo());
                })
                .concatMap(cartDtoGroups -> {
                    return Flux.just(cartDtoGroups.key())
                            .zipWith(cartDtoGroups.collectList())
                            .map(ass -> ass.getT1().withCartList(ass.getT2()));
                })
        ;
    }

    private  <T> Flux<List<T>> setAPIParamLimit(List<T> t){
        int maxApiCallDataCnt = 10;
        int dataForSize = (int) Math.ceil((double)t.size() / (double)maxApiCallDataCnt);
        List<List<T>> result = new ArrayList<List<T>>();
        for(int i = 0 ; i < dataForSize ; i++){
            if(i < dataForSize - 1){
                result.add(t.subList((i * maxApiCallDataCnt), ((i + 1) * maxApiCallDataCnt)));
            }else{
                result.add(t.subList((i * maxApiCallDataCnt), t.size()));
            }

        }
        log.info("setAPIParamList : {}", result);
        return Flux.fromIterable(result);
    }

    private Mono<List<CartGroupReq>> setAPIParamList(Flux<OmCart> omCartList){
        return omCartList
                .map(data -> {
//                    log.info("First omcartList {}", data.getCartSn());
                    return data;
                })
                .groupBy(omCart ->new CartGroupReq(omCart.getSpdNo(), omCart.getSitmNo(), omCart.getTrNo()))
                .map(groups -> groups.key())
                .collectList()
                ;
    }

    private Flux<OmCart> parallelAPITest(List<CartGroupReq> group, Flux<OmCart> omCartList){
        return cmmn.callAPIObject(
                    "/product/v1/detail/productDetailList?dataType=LIGHT2"
                    , group
                 )
                .bodyToFlux(ProductDtlDto.class)
                .map(data -> data.getData())
                .concatMap(apiDataList ->
                                omCartList.concatMap(omCart -> {
                                    for (OmCart apiData : apiDataList) {
                                        if (apiData.isSameProduct(omCart)) {
                                            cmmn.setCopyData(omCart, apiData);
                                            break;
                                        }
                                    }
                                    return Flux.just(omCart);
                                })
                );
    }
}
