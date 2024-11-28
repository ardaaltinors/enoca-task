package com.enoca.ecommerce.services;

import com.enoca.ecommerce.dtos.OrderRequestDTO;
import com.enoca.ecommerce.dtos.OrderResponseDTO;
import com.enoca.ecommerce.dtos.OrderItemRequestDTO;
import com.enoca.ecommerce.entities.Customer;
import com.enoca.ecommerce.entities.Order;
import com.enoca.ecommerce.entities.OrderItem;
import com.enoca.ecommerce.entities.Product;
import com.enoca.ecommerce.mappers.OrderMapper;
import com.enoca.ecommerce.repositories.CustomerRepository;
import com.enoca.ecommerce.repositories.OrderItemRepository;
import com.enoca.ecommerce.repositories.OrderRepository;
import com.enoca.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        // Fetch the Customer by ID
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + orderRequestDTO.getCustomerId()));

        // Initialize a new Order
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderStatus("PENDING");
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setBillingAddress(orderRequestDTO.getBillingAddress());
        order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        order.setShippingMethod(orderRequestDTO.getShippingMethod());
        order.setOrderDate(LocalDateTime.now());

        // Process OrderItems and collect them into a list
        List<OrderItem> orderItems = orderRequestDTO.getOrderItems().stream().map(itemDTO -> {
            // Fetch the Product by ID
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + itemDTO.getProductId()));

            // Check stock availability
            if (product.getStockQuantity() < itemDTO.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product ID: " + product.getId());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - itemDTO.getQuantity());
            productRepository.save(product);

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());

            return orderItem;
        }).collect(Collectors.toList());

        // Calculate total amount by summing up all OrderItem prices multiplied by their quantities
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Set total amount in Order
        order.setTotalAmount(totalAmount);

        // Save the Order
        Order savedOrder = orderRepository.save(order);

        // Associate OrderItems with the saved Order and save them
        orderItems.forEach(item -> item.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        return orderMapper.toResponseDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
        return orderMapper.toResponseDTO(order);
    }

    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        List<Order> orders = orderRepository.findByCustomer(customer);
        return orders.stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
