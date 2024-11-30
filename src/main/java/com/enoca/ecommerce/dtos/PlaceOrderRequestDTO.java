package com.enoca.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlaceOrderRequestDTO {

    @NotNull(message = "Customer ID is mandatory")
    private Long customerId;

    @NotBlank(message = "Shipping address is mandatory")
    @Size(max = 500, message = "Shipping address can have at most 500 characters")
    private String shippingAddress;

    @NotBlank(message = "Billing address is mandatory")
    @Size(max = 500, message = "Billing address can have at most 500 characters")
    private String billingAddress;

    @NotBlank(message = "Payment method is mandatory")
    @Size(max = 100, message = "Payment method can have at most 100 characters")
    private String paymentMethod;

    @NotBlank(message = "Shipping method is mandatory")
    @Size(max = 100, message = "Shipping method can have at most 100 characters")
    private String shippingMethod;
}
