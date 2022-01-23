package shop.newplace.Users.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shop.newplace.Users.model.entity.Profiles;

@Repository
public interface ProfilesRepository  extends JpaRepository<Profiles, Long> {
	Optional<Profiles> findById(Long id);

	List<Profiles> findAllById(Long userId);
}
