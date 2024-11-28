package com.enoca.ecommerce.repositories;

import com.enoca.ecommerce.entities.Cart;
import com.enoca.ecommerce.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomer(Customer customer);

    boolean existsByCustomer(Customer customer);
}
