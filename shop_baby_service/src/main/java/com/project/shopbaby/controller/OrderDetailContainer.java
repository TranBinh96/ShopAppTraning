package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.ErrorDTO;
import com.project.shopbaby.dtos.OrderDetailDTO;
import com.project.shopbaby.response.OrderDetailListResponse;
import com.project.shopbaby.response.OrderDetailResponse;
import com.project.shopbaby.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailContainer {
    private  final OrderDetailService orderDetailService;
    @GetMapping("")
    public ResponseEntity<?> getAllOrderDetails(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit){
        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("Id").descending());
        Page<OrderDetailResponse> orderDetailResponses = orderDetailService.getAllOrderDetails(pageRequest);
        return  ResponseEntity.ok(OrderDetailListResponse.getFromData(orderDetailResponses));
    }

    @PostMapping
    public ResponseEntity<?> insertOrderDetail(@Valid
                          @RequestBody OrderDetailDTO detailDTO,
                          BindingResult result){
       try {
           if (result.hasErrors()){
               List<String> errorMessage =  result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
               return  ResponseEntity.badRequest().body(errorMessage);
           }
           OrderDetailResponse  orderDetailResponse =  orderDetailService.insertOrderDetail(detailDTO);
           return  ResponseEntity.ok(orderDetailResponse);
       }catch (Exception exception){
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
       }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
             @Valid
             @PathVariable("id") long id,
             @RequestBody OrderDetailDTO detailDTO,
             BindingResult result){
       try{
           if (result.hasErrors())
               return  ResponseEntity.badRequest().body(ErrorDTO.getErrorMessage(result));

           OrderDetailResponse orderDetailResponse = orderDetailService.editOrderDetailById(id,detailDTO);

           return  ResponseEntity.ok(orderDetailResponse);
       }catch (Exception exception){
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
       }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("id") long id){
        try{
            return  ResponseEntity.ok(orderDetailService.removeOrderDetailById(id));

        }catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }


}
