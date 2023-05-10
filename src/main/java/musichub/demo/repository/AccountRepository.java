package musichub.demo.repository;

import musichub.demo.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByAccountID(Long id);

    Boolean existsByEmail(String email);

    List<Account> findByAccountIDIn(List<Long> longs);
}