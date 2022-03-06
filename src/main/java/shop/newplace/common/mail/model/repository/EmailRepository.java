package shop.newplace.common.mail.model.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.newplace.common.mail.model.entity.EmailAuthenticationToken;

public interface EmailRepository extends JpaRepository<EmailAuthenticationToken, Long> {

	Optional<EmailAuthenticationToken> findByUserId(Long userId);
	
	Optional<EmailAuthenticationToken> findByIdAndExpirationDateTimeAfterAndExpired(Long id,LocalDateTime now, boolean expired);
	
	
}
