package shop.newplace.users.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shop.newplace.users.model.entity.Profiles;
import shop.newplace.users.model.entity.Users;

@Repository
public interface ProfilesRepository  extends JpaRepository<Profiles, Long> {
	Optional<Profiles> findById(Long id);

	List<Profiles> findAllById(Long id);

	List<Profiles> findAllByUsers(Users usersId);

	long countByUsers(Users users);
	
	long countByUsersId(Long id);
}
