package com.project.shopbaby.controller;

import com.project.shopbaby.dtos.ProductDTO;
import jakarta.validation.Valid;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @GetMapping("")
    public ResponseEntity<?> getAllProduct(@RequestParam("page") int page,
                                           @RequestParam("limit") int limit){
        return  ResponseEntity.ok(String.format("Page %d , Limit %d",page,limit));
    }

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertProduct(@Valid  @ModelAttribute
                           ProductDTO productDTO,
                           BindingResult result) throws IOException {
        if (result.hasErrors()){
            List<String> errorMessage  =  result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        MultipartFile file = productDTO.getFile();
        if (file!= null){
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
            System.out.println(fileName);
        }


        return  ResponseEntity.ok("Insert success");
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> editProduct(@PathVariable("id") int id){
        return  ResponseEntity.ok("Edit Product id");
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteProduct(@PathVariable("id") int id){
        return  ResponseEntity.ok("Delete Product id");
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

}
