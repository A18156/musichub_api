package musichub.demo.repository;

import musichub.demo.model.ERole;
import musichub.demo.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByName(ERole name);
}