package shop.newplace.Users.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shop.newplace.Users.model.entity.Users;

@Repository
public interface UsersRepository  extends JpaRepository<Users, Long> {
	public Optional<Users> findByLoginEmail(String loginEmail);
	public boolean existsByLoginEmail(String loginEmail);

}
