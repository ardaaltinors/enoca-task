package com.enoca.ecommerce.mappers;

import com.enoca.ecommerce.dtos.OrderRequestDTO;
import com.enoca.ecommerce.dtos.OrderResponseDTO;
import com.enoca.ecommerce.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    OrderResponseDTO toResponseDTO(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "shippedDate", ignore = true)
    @Mapping(target = "deliveredDate", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    void updateEntityFromDto(OrderRequestDTO dto, @MappingTarget Order order);
}
