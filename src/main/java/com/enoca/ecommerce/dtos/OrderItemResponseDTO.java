package com.enoca.ecommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {

    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
