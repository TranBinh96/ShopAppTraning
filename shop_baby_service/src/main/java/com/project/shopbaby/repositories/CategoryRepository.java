package com.project.shopbaby.repositories;

import com.project.shopbaby.models.Category;
import com.project.shopbaby.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category,Long> {


}
