package shopbaby.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import shopbaby.models.ProductImage;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
}
