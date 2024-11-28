package com.enoca.ecommerce.repositories;

import com.enoca.ecommerce.entities.Cart;
import com.enoca.ecommerce.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);
}
