// File: /src/main/java/com/enoca/ecommerce/services/CartItemService.java

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
        Cart cart = cartRepository.findById(cartItemRequestDTO.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartItemRequestDTO.getCartId()));

        Product product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + cartItemRequestDTO.getProductId()));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequestDTO.getQuantity());
        cartItem.setPrice(product.getPrice());

        // Calculate totalPrice
        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(cartItemRequestDTO.getQuantity()));

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        CartItemResponseDTO responseDTO = cartItemMapper.toResponseDTO(savedCartItem);
        responseDTO.setTotalPrice(totalPrice);

        return responseDTO;
    }

    @Transactional
    public CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO cartItemRequestDTO) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found with ID: " + id));

        Product product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + cartItemRequestDTO.getProductId()));

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
