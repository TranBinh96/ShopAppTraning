package com.project.shopbaby.services;

import com.project.shopbaby.dtos.CategoryDTO;
import com.project.shopbaby.models.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategory();
    Category updateCategory(long id, CategoryDTO categoryDTO);
    void deleteCategory(long id);
}
