package com.project.shopbaby.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class CategoryDTO {
    @NotEmpty(message = "Category's name can't be empty")
    public  String name;



}
