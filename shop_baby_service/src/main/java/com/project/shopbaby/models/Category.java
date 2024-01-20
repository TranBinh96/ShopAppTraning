package com.project.shopbaby.models;

import com.project.shopbaby.dtos.CategoryDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public  static  Category getFromData(CategoryDTO categoryDTO){
        Category newcategory =  Category.builder()
                .name(categoryDTO.getName())
                .build();
        return  newcategory;
    }

}
