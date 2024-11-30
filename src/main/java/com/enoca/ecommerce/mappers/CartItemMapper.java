package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.CartItemRequestDTO;
import com.enoca.ecommerce.dtos.CartItemResponseDTO;
import com.enoca.ecommerce.entities.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product.id", target = "productId")
    CartItemResponseDTO toResponseDTO(CartItem cartItem);

    CartItem toEntity(CartItemRequestDTO dto);

    void updateEntityFromDto(CartItemRequestDTO dto, @MappingTarget CartItem cartItem);

    @AfterMapping
    default void calculateTotalPrice(CartItem cartItem, @MappingTarget CartItemResponseDTO responseDTO) {
        if (cartItem.getPrice() != null && cartItem.getQuantity() != null) {
            responseDTO.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        } else {
            responseDTO.setTotalPrice(BigDecimal.ZERO);
        }
    }
}
