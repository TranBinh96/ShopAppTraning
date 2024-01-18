package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.ErrorDTO;
import com.project.shopbaby.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailContainer {
    @GetMapping("")
    public ResponseEntity<?> getAllOrderDetails(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit){

        return  ResponseEntity.ok(String.format("Get All OrderDetails Page %d Limit %d",page,limit));
    }

    @PostMapping
    public ResponseEntity<?> insertOrderDetail(@Valid
                          @RequestBody OrderDetailDTO detailDTO,
                          BindingResult result){
        if (result.hasErrors())
            return  ResponseEntity.badRequest().body(ErrorDTO.getErrorMessage(result));

        return  ResponseEntity.ok("Insert OrderDetails Success");

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
             @Valid
             @PathVariable("id") long id,
             @RequestBody OrderDetailDTO detailDTO,
             BindingResult result){
        if (result.hasErrors())
            return  ResponseEntity.badRequest().body(ErrorDTO.getErrorMessage(result));
        return  ResponseEntity.ok("Edit OrderDetails Success");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("id") long id){
        return  ResponseEntity.ok("Delete OrderDetails Success");

    }


}
