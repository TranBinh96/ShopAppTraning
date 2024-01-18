package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.CategoryDTO;
import com.project.shopbaby.dtos.ErrorDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
public class CategoryController {
    @GetMapping("")
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        return ResponseEntity.ok(String.format("Page : %d, Limit : %d ",page,limit));

    }

    @PostMapping("")
    public  ResponseEntity<?> insertCategories(@Valid
            @RequestBody  CategoryDTO dto,
            BindingResult result){
        if (result.hasErrors()){
            return  ResponseEntity.badRequest().body(ErrorDTO.getErrorMessage(result));
        }
        return  ResponseEntity.ok("this is insertCategory"+dto.name);
    }
    @PutMapping("/{id}")
    public  ResponseEntity<String> editCategories(@PathVariable Long id){
        return  ResponseEntity.ok("this is editCategory with id :"+id);
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteCategories(@PathVariable Long id){
        return  ResponseEntity.ok("this is deleteCategory with id : "+id);
    }


}
