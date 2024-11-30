package com.enoca.ecommerce.controllers;

import com.enoca.ecommerce.dtos.CartRequestDTO;
import com.enoca.ecommerce.dtos.CartResponseDTO;
import com.enoca.ecommerce.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(@Valid @RequestBody CartRequestDTO cartRequestDTO) {
        CartResponseDTO created = cartService.createCart(cartRequestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CartResponseDTO> getCartByCustomerId(@PathVariable Long customerId) {
        CartResponseDTO cart = cartService.getCartByCustomerId(customerId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/empty/{customerId}")
    public ResponseEntity<Void> emptyCart(@PathVariable Long customerId) {
        cartService.emptyCart(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
