package com.project.shopbaby.response;
import com.project.shopbaby.models.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends  BaseResponse {
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    private Long categoryId;

    public  static  ProductResponse getFromData(Product product){
        ProductResponse productResponse = ProductResponse
                .builder()
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());


        return  productResponse;

    }



}
