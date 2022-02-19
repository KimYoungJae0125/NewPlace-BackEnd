package shop.newplace.common.mail.model.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.newplace.common.mail.model.entity.EmailAuthenticationToken;

public interface EmailRepository extends JpaRepository<EmailAuthenticationToken, Long> {

	Optional<EmailAuthenticationToken> findByIdAndExpirationDateAfterAndExpired(Long id,LocalDateTime now, boolean expired);
	
	
}
