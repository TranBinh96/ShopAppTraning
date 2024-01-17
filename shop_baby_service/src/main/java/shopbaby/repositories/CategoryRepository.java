package shopbaby.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopbaby.models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
