package com.enoca.ecommerce.repositories;

import com.enoca.ecommerce.entities.Order;
import com.enoca.ecommerce.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
