package com.enoca.ecommerce.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
