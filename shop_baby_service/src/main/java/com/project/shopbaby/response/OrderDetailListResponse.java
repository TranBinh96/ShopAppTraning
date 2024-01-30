package com.project.shopbaby.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailListResponse {
    List<OrderDetailResponse> orderDetail;
    int sumPage;


   public static OrderDetailListResponse getFromData(Page<OrderDetailResponse> orderDetailResponses){

       return OrderDetailListResponse.builder()
               .orderDetail(orderDetailResponses.getContent())
               .sumPage(orderDetailResponses.getTotalPages())
               .build();

   }
}
