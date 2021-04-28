package com.product.product.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartGroupReq {
    private String spdNo;
    private String sitmNo;
    private String trNo;
}
