package shopbaby.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import shopbaby.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    Page<Product> findAll(Pageable pageable);//phân trang
}
