package com.project.shopbaby.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data //String
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {
    List<ProductResponse> products;
    int totalPages;

    public static  ProductListResponse getFromData(List<ProductResponse> responseList, int totalPages){
        return  ProductListResponse.builder()
                .products(responseList)
                .totalPages(totalPages)
                .build();
    }
}
