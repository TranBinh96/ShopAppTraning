package com.project.shopbaby.repositories;

import com.project.shopbaby.models.Order;
import com.project.shopbaby.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
    Page<Order> findAll(Pageable pageable);

    Order findOrderById(Long Id);

    List<Order> findOrderByUserId(Long user_id);
}
