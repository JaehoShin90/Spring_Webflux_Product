package com.product.product.Repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.Database;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MmbrAuthChkRepositoryImpl implements MmbrAuthChkRepository{
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<Long> checkAuthority(String mmbrId, String uri) {
        DatabaseClient dc = template.getDatabaseClient();
        return dc.sql("select count(r.rt_grp_no) cnt \r\n"
                + "   from mmbr m\r\n"
                + "      , st_rt_info r \r\n"
                + "      , st_rt_tgt_base tb \r\n"
                + "  where m.rt_grp_no = r.rt_grp_no \r\n"
                + "    and r.rt_tgt_seq = tb.rt_tgt_seq\r\n"
                + "    and m.mmbr_id = :mmbrId\r\n"
                + "    and r.use_yn = 'Y'\r\n"
                + "    and tb.call_url = :callUrl")
                .bind("mmbrId", mmbrId)
                .bind("callUrl", uri)
                .fetch()
                .one()
                .flatMap(data -> Mono.just(Long.valueOf(data.get("cnt").toString())));
    }
}
