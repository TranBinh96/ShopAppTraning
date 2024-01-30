package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.ErrorDTO;
import com.project.shopbaby.dtos.OrderDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.models.Order;
import com.project.shopbaby.repositories.OrderRepository;
import com.project.shopbaby.response.OrderListResponse;
import com.project.shopbaby.response.OrderResponse;
import com.project.shopbaby.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderControler {
    private  final OrderService orderService;
    @GetMapping("")
    public ResponseEntity<?> getAllOrders(@RequestParam("page") int page,
                                          @RequestParam("limit") int limit) {

        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("Id").descending());

        Page<OrderResponse> orderResponses =  orderService.getAllOrders(pageRequest);

        int pageSum =  orderResponses.getTotalPages();
        List<OrderResponse> responseList = orderResponses.getContent();
        return  ResponseEntity.ok(OrderListResponse.builder().Orders(responseList).totalPage(pageSum).build());
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersById(@Valid @PathVariable("user_id") long userId) {
        try{
            return  ResponseEntity.ok(orderService.getOrderByUserId(userId));

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());

        }

    }

    @PostMapping("")
    public ResponseEntity<?> insertOrders(@Valid @RequestBody OrderDTO orderDTO , BindingResult result) {
        try{
            if (result.hasErrors()){
                return  ResponseEntity.badRequest().body(ErrorDTO.getErrorMessage(result));
            }
            OrderResponse orderRepository =  orderService.createOrder(orderDTO);
            return  ResponseEntity.ok(orderRepository);
        }
        catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOrder(@Valid
           @PathVariable long id,
           @RequestBody OrderDTO orderDTO,
           BindingResult result)  {
        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage).toList();

                return ResponseEntity.badRequest().body(errorMessage);
            }
            return  ResponseEntity.ok(orderService.updateOrder(id,orderDTO));
        }catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid  @PathVariable long id){
        try {
            OrderResponse orderResponse = orderService.deleteOrder(id);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

}
