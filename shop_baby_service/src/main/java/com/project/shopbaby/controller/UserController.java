package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.UserDTO;
import com.project.shopbaby.dtos.UserLoginDTO;
import com.project.shopbaby.models.User;
import com.project.shopbaby.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        try{
            if (result.hasErrors()){
                List<String> errorMessage = result
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return  ResponseEntity.badRequest().body(errorMessage);
            }
            User user = userService.createUser(userDTO);

            return  ResponseEntity.ok(user);
        }catch (Exception exception){
            return  ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public  ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userDTO, BindingResult bindingResult){
        try{
            if (bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return  ResponseEntity.badRequest().body(errorMessage);
            }

            return  ResponseEntity.ok(userService.Login(userDTO));

        }    catch (Exception exception){
            return  ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}
