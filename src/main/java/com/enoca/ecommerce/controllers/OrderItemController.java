package com.enoca.ecommerce.controllers;

import com.enoca.ecommerce.dtos.OrderItemRequestDTO;
import com.enoca.ecommerce.dtos.OrderItemResponseDTO;
import com.enoca.ecommerce.services.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItemResponseDTO> createOrderItem(@Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO) {
        OrderItemResponseDTO createdOrderItem = orderItemService.addOrderItem(orderItemRequestDTO);
        return new ResponseEntity<>(createdOrderItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponseDTO> getOrderItemById(@PathVariable Long id) {
        OrderItemResponseDTO orderItem = orderItemService.getOrderItemById(id);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponseDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemResponseDTO> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        if (orderItems.isEmpty()) {
            return new ResponseEntity<>(orderItems, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }
}
