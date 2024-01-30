package com.project.shopbaby.repositories;

import com.project.shopbaby.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRespository extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductId(Long Id);
}
