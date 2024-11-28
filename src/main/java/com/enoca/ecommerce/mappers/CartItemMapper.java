package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.CartItemRequestDTO;
import com.enoca.ecommerce.dtos.CartItemResponseDTO;
import com.enoca.ecommerce.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemResponseDTO toResponseDTO(CartItem cartItem);

    CartItem toEntity(CartItemRequestDTO dto);

    void updateEntityFromDto(CartItemRequestDTO dto, @MappingTarget CartItem cartItem);
}
