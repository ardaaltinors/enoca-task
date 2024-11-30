package com.enoca.ecommerce.services;

import com.enoca.ecommerce.dtos.CartRequestDTO;
import com.enoca.ecommerce.dtos.CartResponseDTO;
import com.enoca.ecommerce.entities.Cart;
import com.enoca.ecommerce.entities.CartItem;
import com.enoca.ecommerce.entities.Customer;
import com.enoca.ecommerce.mappers.CartMapper;
import com.enoca.ecommerce.repositories.CartItemRepository;
import com.enoca.ecommerce.repositories.CartRepository;
import com.enoca.ecommerce.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository,
                       CustomerRepository customerRepository,
                       CartMapper cartMapper,
                       CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.cartMapper = cartMapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CartResponseDTO createCart(CartRequestDTO cartRequestDTO) {
        Customer customer = customerRepository.findById(cartRequestDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + cartRequestDTO.getCustomerId()));

        if (cartRepository.existsByCustomer(customer)) {
            throw new IllegalArgumentException("Cart already exists for this customer.");
        }

        Cart cart = new Cart();
        cart.setCustomer(customer);

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO getCartByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for customer ID: " + customerId));

        return cartMapper.toResponseDTO(cart);
    }

    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new IllegalArgumentException("Cart not found with ID: " + id);
        }
        cartRepository.deleteById(id);
    }

    @Transactional
    public void emptyCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for customer ID: " + customerId));

        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems != null && !cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
            cart.setCartItems(null);
            cartRepository.save(cart);
        }
    }
}
