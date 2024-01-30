package com.project.shopbaby.services;

import com.project.shopbaby.dtos.OrderDetailDTO;
import com.project.shopbaby.response.OrderDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderDetailService {
    Page<OrderDetailResponse> getAllOrderDetails(Pageable pageable);
    OrderDetailResponse getOrderDetailById(Long Id) throws Exception;
    OrderDetailResponse insertOrderDetail(OrderDetailDTO detailDTO) throws Exception;
    OrderDetailResponse editOrderDetailById(Long Id,OrderDetailDTO detailDTO);
    OrderDetailResponse removeOrderDetailById(Long Id);
}
