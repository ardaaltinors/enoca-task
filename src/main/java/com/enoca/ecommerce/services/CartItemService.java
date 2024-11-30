package com.enoca.ecommerce.services;

import com.enoca.ecommerce.dtos.CartItemRequestDTO;
import com.enoca.ecommerce.dtos.CartItemResponseDTO;
import com.enoca.ecommerce.entities.Cart;
import com.enoca.ecommerce.entities.CartItem;
import com.enoca.ecommerce.entities.Product;
import com.enoca.ecommerce.mappers.CartItemMapper;
import com.enoca.ecommerce.repositories.CartItemRepository;
import com.enoca.ecommerce.repositories.CartRepository;
import com.enoca.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository,
                           CartRepository cartRepository,
                           ProductRepository productRepository,
                           CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemMapper = cartItemMapper;
    }

    @Transactional
    public CartItemResponseDTO addCartItem(CartItemRequestDTO cartItemRequestDTO) {
        // Fetch the Cart
        Cart cart = cartRepository.findById(cartItemRequestDTO.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartItemRequestDTO.getCartId()));

        // Fetch the Product
        Product product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + cartItemRequestDTO.getProductId()));

        // Check stock availability
        if (product.getStockQuantity() < cartItemRequestDTO.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + product.getId());
        }

        // Check if CartItem already exists
        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem cartItem;
        if (existingCartItemOpt.isPresent()) {
            // Update existing CartItem
            cartItem = existingCartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDTO.getQuantity());
            // Optionally, update the price if product price has changed
            cartItem.setPrice(product.getPrice());
        } else {
            // Create new CartItem
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequestDTO.getQuantity());
            cartItem.setPrice(product.getPrice());
        }

        // Save the CartItem
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Prepare the response DTO with totalPrice
        CartItemResponseDTO responseDTO = cartItemMapper.toResponseDTO(savedCartItem);
        responseDTO.setTotalPrice(savedCartItem.getPrice().multiply(new BigDecimal(savedCartItem.getQuantity())));

        return responseDTO;
    }

    @Transactional
    public CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO cartItemRequestDTO) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found with ID: " + id));

        Product product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + cartItemRequestDTO.getProductId()));

        // Check stock availability
        if (product.getStockQuantity() < cartItemRequestDTO.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + product.getId());
        }

        cartItem.setQuantity(cartItemRequestDTO.getQuantity());
        cartItem.setPrice(product.getPrice());

        // Calculate totalPrice
        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(cartItemRequestDTO.getQuantity()));

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        CartItemResponseDTO responseDTO = cartItemMapper.toResponseDTO(updatedCartItem);
        responseDTO.setTotalPrice(totalPrice);

        return responseDTO;
    }

    public void deleteCartItem(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new IllegalArgumentException("CartItem not found with ID: " + id);
        }
        cartItemRepository.deleteById(id);
    }
}
