package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.CartRequestDTO;
import com.enoca.ecommerce.dtos.CartResponseDTO;
import com.enoca.ecommerce.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    CartResponseDTO toResponseDTO(Cart cart);

    Cart toEntity(CartRequestDTO dto);

    void updateEntityFromDto(CartRequestDTO dto, @MappingTarget Cart cart);
}
