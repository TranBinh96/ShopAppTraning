package com.project.shopbaby.repositories;

import com.project.shopbaby.models.Order;
import com.project.shopbaby.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository  extends JpaRepository<OrderDetail,Long> {
    OrderDetail findOrderDetailById(Long Id);
}
