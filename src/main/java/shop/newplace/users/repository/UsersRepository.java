package shop.newplace.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shop.newplace.users.model.entity.Users;

@Repository
public interface UsersRepository  extends JpaRepository<Users, Long> {
	Optional<Users> findByLoginEmail(String loginEmail);
	Optional<Users> findById(Long id);
	boolean existsById(Long id);
	boolean existsByLoginEmail(String loginEmail);
	Optional<Users> findAllByLoginEmail(String loginEmail);
	Optional<Users> findAllById(Long id);
	

}
