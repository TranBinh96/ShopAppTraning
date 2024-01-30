package com.project.shopbaby.services;

import com.project.shopbaby.dtos.OrderDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.repositories.OrderRepository;
import com.project.shopbaby.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse updateOrder(Long Id, OrderDTO orderDTO) throws DataNotFoundException;
    OrderRepository getOrderById(Long Id);

    List<OrderResponse> getOrderByUserId(Long user_id) throws Exception;

    OrderResponse deleteOrder(Long Id) throws DataNotFoundException;

    Page<OrderResponse> getAllOrders(Pageable pageable);
}
