package com.enoca.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequestDTO {

    @NotNull(message = "Customer ID is mandatory")
    private Long customerId;
}
