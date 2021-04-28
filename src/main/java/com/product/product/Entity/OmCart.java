package com.product.product.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.relational.core.mapping.Table;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EnableR2dbcAuditing
@Table("om_cart")
public class OmCart {

    @Id
    private String cartSn;
    private String mbNo;
    private int odQty;

    private String trNo;
    private String lrtrNo;
    private String spdNo;
    private String sitmNo;

    // reactiveAuditorAware
    @CreatedDate
    private LocalDateTime regDttm;
    @LastModifiedDate
    private LocalDateTime modDttm;

    @With
    @Transient
    private List<OmCart> cartList;



    @Transient
    private String brdNm;
    @Transient
    private String spdNm;
    @Transient
    private int slPrc;
    @Transient
    private String sitmNm;
    @Transient
    private String estmtDlvTxt;
    @Transient
    private String returnCode;
    @Transient
    private String message;
    @Transient
    private List<Map<String, Object>> imgJsn;
    @Transient
    private Map<String, Object> itmByMaxPurPsbJsn;

    public boolean isSameProduct (OmCart target){
        return this.getSpdNo().equals(target.getSpdNo()) && this.getSitmNo().equals(target.getSitmNo()) && this.getTrNo().equals(target.getTrNo());
    }
}
