package com.project.shopbaby.services;

import com.project.shopbaby.dtos.OrderDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.models.Order;
import com.project.shopbaby.models.OrderStatus;
import com.project.shopbaby.models.User;
import com.project.shopbaby.repositories.OrderRepository;
import com.project.shopbaby.repositories.UserRespository;
import com.project.shopbaby.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private   final OrderRepository orderRepository;
    private   final UserRespository userRespository;
    private  final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {

        Optional<User> existUser = Optional.ofNullable(userRespository
                .findById(orderDTO
                        .getUserId())
                .orElseThrow(() -> new DataNotFoundException(
                        "User Not Found Id :"+orderDTO.getUserId())));
        // convert  OrderDTO -> Order
        Order order = new Order();
        modelMapper.typeMap(OrderDTO.class,Order.class)
                .addMappings(mapper-> mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        order.setUser(existUser.get());
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // check shipping_date > today
        LocalDate shippingDate =   orderDTO.getShippingDate() == null ? LocalDate.now() :orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now()))
        {
            throw  new  Exception("Date must be al least today !");
        }
        order.setActive(true);
        orderRepository.save(order);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);
        orderResponse.setUser_id(order.getUser().getId());
        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(Long Id, OrderDTO orderDTO) throws DataNotFoundException {
        Optional<Order> order = Optional.ofNullable(orderRepository
                .findById(Id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find by order with Id : "+Id)));

        Optional<User> existUser = Optional.ofNullable(userRespository
                .findById(orderDTO
                        .getUserId())
                .orElseThrow(() -> new DataNotFoundException(
                        "User Not Found Id :"+orderDTO.getUserId())));

        Order newOrder = order.get();
        modelMapper
                .typeMap(OrderDTO.class,Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO,newOrder);
        newOrder.setUser(existUser.get());
        orderRepository.save(newOrder);
        OrderResponse orderResponse = modelMapper.map(newOrder,OrderResponse.class);
        orderResponse.setUser_id(newOrder.getUser().getId());
        return orderResponse;
    }

    @Override
    public OrderRepository getOrderById(Long Id) {
        return null;
    }
    @Override
    public List<OrderResponse> getOrderByUserId(Long user_id) throws Exception {
        List<Order>  orderList = orderRepository.findOrderByUserId(user_id);
        if (orderList.size() == 0)
            throw  new Exception("Cann't Order with User  : "+user_id );
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orderList) {
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
            orderResponse.setUser_id(order.getUser().getId());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public OrderResponse deleteOrder(Long Id) throws DataNotFoundException {
        Optional<Order> order = Optional.ofNullable(orderRepository
                .findById(Id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find By Order With Id : "+Id)));

        if (order!= null){
            order.get().setActive(false);
            orderRepository.save(order.get());
        }

       OrderResponse orderResponse =  modelMapper.map(order,OrderResponse.class);
       orderResponse.setUser_id(order.get().getUser().getId());

       return orderResponse;
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {

        return orderRepository.findAll(pageable)
                .map( order -> {
                    OrderResponse orderResponse = new OrderResponse();
                    modelMapper.map(order,orderResponse);
                    orderResponse.setUser_id(order.getUser().getId());
                    return  orderResponse;
                });

    }
}
