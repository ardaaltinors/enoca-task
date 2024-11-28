package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.ProductRequestDTO;
import com.enoca.ecommerce.dtos.ProductResponseDTO;
import com.enoca.ecommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Map from Product to ProductResponseDTO
    ProductResponseDTO toResponseDTO(Product product);

    // Map from ProductRequestDTO to Product
    Product toEntity(ProductRequestDTO dto);

    // Update existing Product entity with ProductRequestDTO data
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget Product product);
}
