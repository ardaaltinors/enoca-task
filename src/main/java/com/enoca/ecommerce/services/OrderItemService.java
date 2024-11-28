package com.enoca.ecommerce.services;

import com.enoca.ecommerce.dtos.OrderItemRequestDTO;
import com.enoca.ecommerce.dtos.OrderItemResponseDTO;
import com.enoca.ecommerce.entities.Order;
import com.enoca.ecommerce.entities.OrderItem;
import com.enoca.ecommerce.entities.Product;
import com.enoca.ecommerce.mappers.OrderItemMapper;
import com.enoca.ecommerce.repositories.OrderItemRepository;
import com.enoca.ecommerce.repositories.OrderRepository;
import com.enoca.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemMapper = orderItemMapper;
    }

    @Transactional
    public OrderItemResponseDTO addOrderItem(OrderItemRequestDTO orderItemRequestDTO) {
        // Fetch the Order by ID
        Order order = orderRepository.findById(orderItemRequestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderItemRequestDTO.getOrderId()));

        // Fetch the Product by ID
        Product product = productRepository.findById(orderItemRequestDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + orderItemRequestDTO.getProductId()));

        // Check if the product has enough stock
        if (product.getStockQuantity() < orderItemRequestDTO.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + product.getId());
        }

        // Reduce the stock
        product.setStockQuantity(product.getStockQuantity() - orderItemRequestDTO.getQuantity());
        productRepository.save(product);

        // Create a new OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        orderItem.setPrice(product.getPrice());

        // Calculate total price for this OrderItem
        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(orderItemRequestDTO.getQuantity()));
        orderItem.setPrice(totalPrice);

        // Save the OrderItem
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return orderItemMapper.toResponseDTO(savedOrderItem);
    }

    public OrderItemResponseDTO getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found with ID: " + id));
        return orderItemMapper.toResponseDTO(orderItem);
    }

    public List<OrderItemResponseDTO> getOrderItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        return orderItems.stream()
                .map(orderItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
