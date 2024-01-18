package com.project.shopbaby.dtos;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorDTO {

    public  static List<String> getErrorMessage(@Valid BindingResult result){
        List<String> errorMessage =  result
                .getFieldErrors()
                .stream()
                .map(FieldError:: getDefaultMessage)
                .toList();
        return  errorMessage;

    }
}
