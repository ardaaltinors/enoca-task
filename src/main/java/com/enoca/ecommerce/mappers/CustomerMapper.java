package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.CustomerRequestDTO;
import com.enoca.ecommerce.dtos.CustomerResponseDTO;
import com.enoca.ecommerce.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    // Convert Entity to Response DTO
    public CustomerResponseDTO toResponseDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        return dto;
    }

    // Convert Request DTO to Entity
    public Customer toEntity(CustomerRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        return customer;
    }

    // Update Entity with Request DTO
    public void updateEntity(Customer customer, CustomerRequestDTO dto) {
        if (dto == null || customer == null) {
            return;
        }
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
    }
}
