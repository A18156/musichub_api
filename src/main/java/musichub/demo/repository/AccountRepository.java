package musichub.demo.repository;

import musichub.demo.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findByUsername(String username);

    Account findAccountByAccountID(Long id);
    Account findAccountByEmail(String mail);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    List<Account> findByEmailAndPhoneNot(String email, String phone);
    Integer countByEmailAndAccountIDNot(String email, Long accId);
    Integer countByPhoneAndAccountIDNot(String phone, Long accId);
//    List<Account> findByPhoneNotIn(String phone);
//    List<Account> findByAccountIDIn(List<Long> longs);
}