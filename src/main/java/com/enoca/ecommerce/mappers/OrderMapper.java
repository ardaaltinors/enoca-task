package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.OrderRequestDTO;
import com.enoca.ecommerce.dtos.OrderResponseDTO;
import com.enoca.ecommerce.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    OrderResponseDTO toResponseDTO(Order order);

    Order toEntity(OrderRequestDTO dto);

    void updateEntityFromDto(OrderRequestDTO dto, @MappingTarget Order order);
}
