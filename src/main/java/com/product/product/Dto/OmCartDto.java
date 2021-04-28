package com.product.product.Dto;

import com.product.product.Entity.OmCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmCartDto {

    private String mbNo;
    private String trNo;

    @With
    @Transient
    private List<OmCart> cartList;

    public OmCartDto(String mbNo, String trNo) {
        this.mbNo = mbNo;
        this.trNo = trNo;
    }
}
