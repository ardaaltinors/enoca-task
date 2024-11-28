package com.enoca.ecommerce.controllers;

import com.enoca.ecommerce.dtos.CartItemRequestDTO;
import com.enoca.ecommerce.dtos.CartItemResponseDTO;
import com.enoca.ecommerce.services.CartItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> addCartItem(@Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        CartItemResponseDTO created = cartItemService.addCartItem(cartItemRequestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> updateCartItem(@PathVariable Long id,
                                                              @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        CartItemResponseDTO updated = cartItemService.updateCartItem(id, cartItemRequestDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
