package com.enoca.ecommerce.repositories;

import com.enoca.ecommerce.entities.Customer;
import com.enoca.ecommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(Customer customer);
}
