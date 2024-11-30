package com.enoca.ecommerce.services;

import com.enoca.ecommerce.dtos.OrderResponseDTO;
import com.enoca.ecommerce.entities.*;
import com.enoca.ecommerce.mappers.OrderMapper;
import com.enoca.ecommerce.repositories.*;
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
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponseDTO placeOrder(Long customerId) {
        // Get customer by idş
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        // get customers cart
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for customer ID: " + customerId));

        // get all items in the cart
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty for customer ID: " + customerId);
        }

        // Initialize a new Order
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderStatus("PENDING");
        order.setShippingAddress("Default Shipping Address");
        order.setBillingAddress("Default Billing Address");
        order.setPaymentMethod("Credit Card");
        order.setShippingMethod("HepsiJet");
        order.setOrderDate(LocalDateTime.now());

        // Process CartItems and create OrderItems
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            // Check stock availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product ID: " + product.getId());
            }

            // reduce stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));

            return orderItem;
        }).collect(Collectors.toList());

        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Set total amount in Order
        order.setTotalAmount(totalAmount);

        // Save the Order
        Order savedOrder = orderRepository.save(order);

        // Associate OrderItems with the saved Order and save them
        orderItems.forEach(item -> item.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        // clear users cart
        cartItemRepository.deleteAll(cartItems);

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
