package com.product.product.Dto;

import com.product.product.Entity.OmCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtlDto {
    private String returnCode;
    private String message;
    private String[] subMessages;
    private String messageType;
    private int dataCount;
    private OmCart[] data;
}
