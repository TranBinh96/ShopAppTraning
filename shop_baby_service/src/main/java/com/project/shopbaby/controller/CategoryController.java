package com.project.shopbaby.controller;

import com.github.javafaker.Faker;
import com.project.shopbaby.dtos.CategoryDTO;
import com.project.shopbaby.dtos.ErrorDTO;
import com.project.shopbaby.models.Category;
import com.project.shopbaby.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
//@Validated
public class CategoryController {
    private final  CategoryService categoryService;

    @PostMapping("")
    public  ResponseEntity<?> insertCategories(@Valid
                                               @RequestBody  CategoryDTO categoryDTO,
                                               BindingResult result){
        if (result.hasErrors()){
            return  ResponseEntity.badRequest().body(ErrorDTO.getErrorMessage(result));
        }
        Category category= categoryService.createCategory(categoryDTO);
        return  ResponseEntity.ok(category.toString());
    }

    @GetMapping("")
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        return ResponseEntity.ok(categoryService.getAllCategory().toString());

    }


    @PutMapping("/{id}")
    public  ResponseEntity<String> editCategories(@PathVariable Long id,@RequestBody  CategoryDTO categoryDTO ){
        Category category = categoryService.updateCategory(id,categoryDTO);
        return  ResponseEntity.ok(category.toString());
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteCategories(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return  ResponseEntity.ok("this is deleteCategory with id : "+id);
    }

    //fack data category

    @PostMapping("/generateFakeCategory")
    public  ResponseEntity<?> generateCategory(){
        Faker faker = new Faker();
        for (int i=0;i<1000;i++){
            CategoryDTO categoryDTO =CategoryDTO
                    .builder()
                    .name(faker.commerce().material())
                    .build();
            categoryService.createCategory(categoryDTO);

        }
        return  ResponseEntity.ok("Generate Category Success");


    }


}
