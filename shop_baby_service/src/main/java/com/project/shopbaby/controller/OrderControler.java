package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.ErrorDTO;
import com.project.shopbaby.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderControler {
    @GetMapping("")
    public ResponseEntity<?> getAllOrders(@RequestParam("page") int page,
                                          @RequestParam("limit") int limit) {

        return  ResponseEntity.ok(String.format("Danh Sach Orders : Page %d Limit %d",page,limit));
    }

    @PostMapping("")
    public ResponseEntity<?> insertOrders(@Valid @RequestBody OrderDTO orderDTO , BindingResult result) {
        if (result.hasErrors()){
            return  ResponseEntity.badRequest().body(ErrorDTO.getErrorMessage(result));
        }
        return  ResponseEntity.ok(orderDTO.toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOrder(@Valid
           @PathVariable long id,
           @RequestBody OrderDTO orderDTO,
           BindingResult result){

        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage).toList();

            return ResponseEntity.badRequest().body(errorMessage);
        }
        return  ResponseEntity.ok(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid  @PathVariable long id){
        return  ResponseEntity.ok(id);
    }

}
