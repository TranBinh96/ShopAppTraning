package com.project.shopbaby.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDetailResponse {
   private Long id;
    @JsonProperty("order_id")
    private Long order_id;

    @JsonProperty("product_id")
    private Long product_id;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("color")
    private String color;


}
