package com.project.shopbaby.controller;

import com.github.javafaker.Faker;
import com.project.shopbaby.dtos.CategoryDTO;
import com.project.shopbaby.dtos.ProductDTO;
import com.project.shopbaby.dtos.ProductImageDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.models.Product;
import com.project.shopbaby.models.ProductImage;
import com.project.shopbaby.response.ProductListResponse;
import com.project.shopbaby.response.ProductResponse;
import com.project.shopbaby.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<?> getAllProduct(@RequestParam("page") int page,
                                           @RequestParam("limit") int limit){
        PageRequest pageRequest = PageRequest
                .of(page,limit,Sort.by("createdAt")
                .descending());

        Page<ProductResponse> productPages = productService.getAllProducts(pageRequest);
        int totalPages = productPages.getTotalPages();
        List<ProductResponse> products = productPages.getContent();

        return  ResponseEntity.ok(ProductListResponse.getFromData(products,totalPages));
    }
    @PostMapping(value = "")
    public ResponseEntity<?> insertProduct(@Valid  @RequestBody
                           ProductDTO productDTO,
                           BindingResult result) throws IOException {
        try{
            if (result.hasErrors()){
                List<String> errorMessage  =  result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }


            Product product = productService.createProduct(productDTO);

            return  ResponseEntity.ok(ProductResponse.getFromData(product));

        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            value = "/insertImage/{id}"
            )
    public  ResponseEntity<?> insertProductImage(@PathVariable("id") long id,
                             @ModelAttribute List<MultipartFile> files){
        try{
            files = files==null ? new ArrayList<MultipartFile>(): files;
            ProductImage productImage= null;
            if (files.size()> ProductImage.MAXIMUM_IMAGES_PER_PRODUCT)
                return  ResponseEntity.badRequest().body(String.format("You can only upload maximum %s images"+ProductImage.MAXIMUM_IMAGES_PER_PRODUCT));

            for (MultipartFile file :files){
                Product existsproduct = productService.getProductById(id);
                if (file.getSize() > 10*1024*1024){
                    return  ResponseEntity
                            .status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Max size is 10MB");
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")){
                    return  ResponseEntity
                            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                String fileName = storeFile(file);
                productImage= productService.createProductImage(ProductImageDTO
                        .builder()
                        .productId(existsproduct.getId())
                        .imageUrl(fileName)
                        .build());
            }
            return  ResponseEntity.ok(productImage);

        }catch (Exception exception){
            return  ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> editProduct(@PathVariable("id") long id,@RequestBody ProductDTO productDTO){
        try{
           Product product=  productService.updateProduct(id,productDTO);
            return  ResponseEntity.ok(ProductResponse.getFromData(product));
        }catch (Exception exception){
            return  ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getProductById(@PathVariable("id") long id){
        try{
            Product product=  productService.getProductById(id);
            return  ResponseEntity.ok(ProductResponse.getFromData(product));
        }catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteProduct(@PathVariable("id") long id){
        try{
            productService.deleteProduct(id);
            return  ResponseEntity.ok(String.format("Remove Product with %s Success",id));
        }catch (Exception exception){
            return  ResponseEntity.badRequest().body(exception.getMessage());
        }

    }
    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }


    @PostMapping("/generateFakeProduct")
    public  ResponseEntity<?> generateCategory() {
        try{
            Faker faker = new Faker();
                for (int i=0;i<1000;i++){
                    String productName = faker.commerce().productName();
                    if (!productService.existsByName(productName)) {
                        ProductDTO categoryDTO = ProductDTO
                                .builder()
                                .name(productName)
                                .price((float) faker.number().numberBetween(10, 90000000))
                                .description(faker.lorem().sentence())
                                .thumbnail("")
                                .categoryId((long) faker.number().numberBetween(6, 999))
                                .build();
                        productService.createProduct(categoryDTO);
                    }
                }

        }catch (Exception exception){
            return  ResponseEntity.badRequest().body(exception.getMessage());
        }
        return  ResponseEntity.ok("Generate Category Success");


    }

}
