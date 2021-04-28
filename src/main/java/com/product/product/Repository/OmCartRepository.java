package com.product.product.Repository;

import com.product.product.Dto.OmCartDto;
import com.product.product.Entity.OmCart;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@EnableR2dbcAuditing
public interface OmCartRepository extends ReactiveCrudRepository<OmCart, String>{
    Flux<OmCart> findByMbNoOrderByModDttmDesc(String mbNo);

    Mono<Integer> countByMbNo(String mbNo);

    @Query("select tr_no, mb_no from om_cart where mb_no = :mbNo group by tr_no, mb_no order by tr_no")
    Flux<OmCartDto> findTrNoByMbNoANDGroupby(String mbNo);

    Flux<OmCart> findByMbNoAndTrNo(String mbNo, String trNo);

    @Query("WITH recursive temp_cart_sn AS \n" +
            "(\tSELECT TO_CHAR(NOW(), 'YYYYMMDDHH24MISS') || LPAD(CAST(TRUNC(RANDOM() * 99 + 1) AS TEXT), 2,'0') AS cartSn, 1 AS num\n" +
            "\tUNION \n" +
            "\tSELECT TO_CHAR(NOW(), 'YYYYMMDDHH24MISS') || LPAD(CAST(TRUNC(RANDOM() * 99 + 1) AS TEXT), 2,'0'), num + 1 \n" +
            "\tFROM temp_cart_sn tcs \n" +
            "\tINNER JOIN om_cart_sn ocs ON tcs.cartSn = ocs.cart_sn\n" +
            ")\n" +
            "INSERT INTO om_cart_sn (cart_sn)\n" +
            "SELECT cartSn FROM temp_cart_sn ORDER BY num DESC LIMIT 1\n" +
            "RETURNING cart_sn")
    Mono<String> getCartSnSeq();
}