package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.UserDTO;
import com.project.shopbaby.dtos.UserLoginDTO;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessage = result
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return  ResponseEntity.badRequest().body(errorMessage);
        }
        return  ResponseEntity.ok("Register Success");
    }

    @PostMapping("/login")
    public  ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<String> errorMessage = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return  ResponseEntity.badRequest().body(errorMessage);
        }
        return  ResponseEntity.ok("Login  Success");
    }

}
