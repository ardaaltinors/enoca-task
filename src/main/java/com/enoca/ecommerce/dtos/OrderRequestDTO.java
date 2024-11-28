package com.enoca.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "Customer ID is mandatory")
    private Long customerId;

    @NotBlank(message = "Shipping address is mandatory")
    private String shippingAddress;

    @NotBlank(message = "Billing address is mandatory")
    private String billingAddress;

    @NotBlank(message = "Payment method is mandatory")
    private String paymentMethod;

    @NotBlank(message = "Shipping method is mandatory")
    private String shippingMethod;

    @NotNull(message = "Order items are mandatory")
    private List<OrderItemRequestDTO> orderItems;
}
