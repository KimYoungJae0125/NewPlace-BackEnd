package shop.newplace.Account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.newplace.Account.model.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String 홍길동);
}
