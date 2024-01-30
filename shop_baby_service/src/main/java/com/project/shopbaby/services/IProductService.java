package com.project.shopbaby.services;

import com.project.shopbaby.dtos.ProductDTO;
import com.project.shopbaby.dtos.ProductImageDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.exceptions.InvalidParamException;
import com.project.shopbaby.models.Product;
import com.project.shopbaby.models.ProductImage;
import com.project.shopbaby.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(long Id) throws DataNotFoundException;

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Product updateProduct(long id , ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(long id) throws DataNotFoundException;

    boolean existsByName(String name);

    ProductImage createProductImage(ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
}
