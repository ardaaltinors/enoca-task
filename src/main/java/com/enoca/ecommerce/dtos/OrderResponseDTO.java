package com.enoca.ecommerce.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long customerId;
    private String orderStatus;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String shippingMethod;
    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private List<OrderItemResponseDTO> orderItems;
}
