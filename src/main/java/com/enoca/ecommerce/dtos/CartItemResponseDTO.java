package com.enoca.ecommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDTO {

    private Long id;
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
