package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.CustomerRequestDTO;
import com.enoca.ecommerce.dtos.CustomerResponseDTO;
import com.enoca.ecommerce.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Map from Customer to CustomerResponseDTO
    CustomerResponseDTO toResponseDTO(Customer customer);

    // Map from CustomerRequestDTO to Customer
    Customer toEntity(CustomerRequestDTO dto);

    // Update existing Customer entity with CustomerRequestDTO data
    void updateEntityFromDto(CustomerRequestDTO dto, @MappingTarget Customer customer);
}
