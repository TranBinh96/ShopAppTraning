package shopbaby.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import shopbaby.models.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
