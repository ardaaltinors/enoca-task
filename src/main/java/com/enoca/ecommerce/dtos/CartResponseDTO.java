package com.enoca.ecommerce.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponseDTO {

    private Long id;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemResponseDTO> cartItems;
}
