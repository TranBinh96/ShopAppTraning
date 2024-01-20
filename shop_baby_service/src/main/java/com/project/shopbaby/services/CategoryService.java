package com.project.shopbaby.services;

import com.project.shopbaby.dtos.CategoryDTO;
import com.project.shopbaby.models.Category;
import com.project.shopbaby.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService  implements  ICategoryService{
    private final CategoryRepository repository;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category.getFromData(categoryDTO);
        return repository.save(category);
    }

    @Override
    public Category getCategoryById(long id) {
        return repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Category Not Found"));
    }

    @Override
    public List<Category> getAllCategory() {
        return repository.findAll();
    }

    @Override
    public Category updateCategory(long id, CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(categoryDTO.getName());
        repository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        //xoá cứng
        repository.deleteById(id);
    }
}
