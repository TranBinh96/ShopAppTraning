package com.project.shopbaby.services;

import com.project.shopbaby.dtos.OrderDetailDTO;
import com.project.shopbaby.models.Order;
import com.project.shopbaby.models.OrderDetail;
import com.project.shopbaby.models.Product;
import com.project.shopbaby.repositories.OrderDetailRepository;
import com.project.shopbaby.repositories.OrderRepository;
import com.project.shopbaby.repositories.ProductRespoditory;
import com.project.shopbaby.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements  IOrderDetailService {
    private  final OrderRepository orderRepository;
    private  final OrderDetailRepository orderDetailRepository;
    private  final ProductRespoditory productRespoditory;
    private  final ModelMapper modelMapper;

    @Override
    public Page<OrderDetailResponse> getAllOrderDetails(Pageable pageable) {
        return orderDetailRepository.findAll(pageable).map(orderDetail -> {
            OrderDetailResponse orderDetailResponse =modelMapper.map(orderDetail,OrderDetailResponse.class);
            orderDetailResponse.setOrder_id(orderDetail.getOrder().getId());
            orderDetailResponse.setProduct_id(orderDetail.getProduct().getId());
            return  orderDetailResponse;
        });
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long Id) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(Id)
                .orElseThrow(()-> new Exception("Cannot find OrderDetail with Id : "+Id));
        OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail,OrderDetailResponse.class);
        orderDetailResponse.setOrder_id(orderDetail.getOrder().getId());
        orderDetailResponse.setProduct_id(orderDetail.getProduct().getId());

        return orderDetailResponse;
    }

    @Override
    public OrderDetailResponse insertOrderDetail(OrderDetailDTO detailDTO) throws Exception {

        Product product = productRespoditory.findProductById(detailDTO.getProductId());

        Order order= orderRepository.findOrderById(detailDTO.getOrderId());

        if (product == null )
            throw  new Exception("Find cannot Product with Id :  "+detailDTO.getProductId());

        if (order == null)
            throw  new Exception("Find cannot Order with Id :  "+detailDTO.getOrderId());

        OrderDetail orderDetail = new OrderDetail();
        modelMapper.typeMap(OrderDetailDTO.class,OrderDetail.class)
                .addMappings(modelMapper -> modelMapper.skip(OrderDetail::setId));

        modelMapper.map(detailDTO,orderDetail);
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setTotalMoney(orderDetail.getPrice() * orderDetail.getNumberOfProducts());
        orderDetailRepository.save(orderDetail);

        OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail,OrderDetailResponse.class);
        orderDetailResponse.setProduct_id(orderDetail.getProduct().getId());
        orderDetailResponse.setOrder_id(orderDetail.getOrder().getId());

        return  orderDetailResponse;
    }

    @Override
    public OrderDetailResponse editOrderDetailById(Long Id, OrderDetailDTO detailDTO) throws Exception {
        OrderDetail existOrderDetail = orderDetailRepository.findOrderDetailById(Id);
        if (existOrderDetail == null)
            throw  new Exception("Cannot find Orderdetail with By ID : "+Id);

        modelMapper.typeMap(OrderDetailDTO.class,OrderDetail.class)
                .addMappings(modelMapper -> modelMapper.skip(OrderDetail :: setId));
        modelMapper.map(detailDTO,existOrderDetail);
        existOrderDetail.setTotalMoney(existOrderDetail.getPrice() * existOrderDetail.getNumberOfProducts());

        orderDetailRepository.save(existOrderDetail);

        OrderDetailResponse orderDetailResponse = modelMapper.map(existOrderDetail,OrderDetailResponse.class);
        orderDetailResponse.setProduct_id(existOrderDetail.getProduct().getId());
        orderDetailResponse.setOrder_id(existOrderDetail.getOrder().getId());

        return orderDetailResponse;
    }

    @Override
    public OrderDetailResponse removeOrderDetailById(Long Id) throws Exception {
        OrderDetail existOrderDetail = orderDetailRepository.findOrderDetailById(Id);
        if (existOrderDetail == null)
            throw  new Exception("Cannot find Orderdetail with By ID : "+Id);

        orderDetailRepository.delete(existOrderDetail);

        OrderDetailResponse orderDetailResponse = modelMapper.map(existOrderDetail,OrderDetailResponse.class);
        orderDetailResponse.setProduct_id(existOrderDetail.getProduct().getId());
        orderDetailResponse.setOrder_id(existOrderDetail.getOrder().getId());

        return null;
    }
}
