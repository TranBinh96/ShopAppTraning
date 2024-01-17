package shopbaby.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import shopbaby.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
