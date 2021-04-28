package com.product.product.Request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmCartReq {
    @Id
    private int cartSn;
    private String mbNo;
    private int odQty;

    private String trNo;
    private String lrtrNo;
    private String spdNo;
    private String sitmNo;

    @CreatedDate
    private LocalDateTime regDttm;
}
