package shop.newplace.Account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.newplace.Account.model.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByName(String 홍길동);
}
