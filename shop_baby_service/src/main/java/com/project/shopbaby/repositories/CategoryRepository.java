package com.project.shopbaby.repositories;

import com.project.shopbaby.models.Category;
import com.project.shopbaby.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category,Long> {

    //Page<Category> findAll(Pageable pageable);

}
