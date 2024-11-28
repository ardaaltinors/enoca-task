package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.OrderItemRequestDTO;
import com.enoca.ecommerce.dtos.OrderItemResponseDTO;
import com.enoca.ecommerce.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderItemResponseDTO toResponseDTO(OrderItem orderItem);

    OrderItem toEntity(OrderItemRequestDTO dto);

    void updateEntityFromDto(OrderItemRequestDTO dto, @MappingTarget OrderItem orderItem);
}
