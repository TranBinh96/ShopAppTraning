package com.project.shopbaby.repositories;

import com.project.shopbaby.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ProductRespoditory extends JpaRepository<Product,Long> {
    boolean existsByName(String title);

    List<Product> findByCategoryId(Long categoryId);

    Product findProductById(long id);


}
