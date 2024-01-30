package com.project.shopbaby.services;

import com.project.shopbaby.dtos.ProductDTO;
import com.project.shopbaby.dtos.ProductImageDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.exceptions.InvalidParamException;
import com.project.shopbaby.models.Category;
import com.project.shopbaby.models.Product;
import com.project.shopbaby.models.ProductImage;
import com.project.shopbaby.repositories.CategoryRepository;
import com.project.shopbaby.repositories.ProductImageRespository;
import com.project.shopbaby.repositories.ProductRespoditory;
import com.project.shopbaby.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements  IProductService{
    private  final ProductRespoditory productRespoditory;
    private  final CategoryRepository categoryRepository;
    private  final ProductImageRespository productImageRespository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existsCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find category with id : "+productDTO.getCategoryId()));
       return    productRespoditory.save(Product.getFromData(productDTO,existsCategory));
    }

    @Override
    public Product getProductById(long Id) throws DataNotFoundException {
      Optional<Product> productOptional = Optional.ofNullable(
              Optional.ofNullable(productRespoditory
              .findProductById(Id))
              .orElseThrow(()-> new
                      DataNotFoundException("Cannot found product with id : "+Id)));
      return  productOptional.get();

    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRespoditory.findAll(pageable).map(
                ProductResponse::getFromData);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {

        Product product =  getProductById(id);
        Category existsCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find category with id : "+productDTO.getCategoryId()));
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setThumbnail(productDTO.getThumbnail());
        product.setCategory(existsCategory);
        productRespoditory.save(product);
        return product;
    }

    @Override
    public void deleteProduct(long id) throws DataNotFoundException {
        Optional<Product> optionalProduct =
                Optional.ofNullable(productRespoditory.findById(id)
                        .orElseThrow(() -> new DataNotFoundException("Cannot found product with id : "+id)));
        optionalProduct.ifPresent(productRespoditory::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRespoditory.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
        Product existsProduct = productRespoditory
                .findById(productImageDTO.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cannot found product with id : "+productImageDTO.getProductId()));

        ProductImage productImage = ProductImage.getFormData(productImageDTO);
        productImage.setProduct(existsProduct);
        // no insert max 5 images give 1 product
        int size  = productImageRespository.findByProductId(productImageDTO.getProductId()).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT)
            throw  new InvalidParamException(
                    "Number of image must be <= "
                    +ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);

        productImageRespository.save(productImage);
        return  productImage;

    }

}
