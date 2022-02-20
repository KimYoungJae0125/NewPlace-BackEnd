package shop.newplace.common.mail.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.SpringBootMail;
import shop.newplace.common.mail.model.entity.EmailAuthenticationToken;
import shop.newplace.common.mail.model.repository.EmailRepository;
import shop.newplace.common.util.CipherUtil;

@RequiredArgsConstructor
@Service
public class EmailAuthenticationService {
	private final EmailRepository emailRepository;
	
	private final SpringBootMail springBootMail;

	private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;
	
	@Transactional
	public void sendEmailAuthentication(Long userId, String reciverEmail) {
		EmailAuthenticationToken emailAuthenticationToken = createEmailAuthenticationToken(userId);
		EmailAuthenticationToken findToken = emailRepository.save(emailAuthenticationToken);
		StringBuilder emailAuthenticationUrl = new StringBuilder();
		emailAuthenticationUrl.append("http://localhost:8080/emailAuthentication/");
		emailAuthenticationUrl.append(findToken.getId());
		springBootMail.sendMail(CipherUtil.Email.decrypt(reciverEmail), "회원가입 이메일 인증", emailAuthenticationUrl.toString());
	}

	
	private EmailAuthenticationToken createEmailAuthenticationToken(Long userId) {
		return EmailAuthenticationToken.builder()
					 	 .expirationDate(LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE)) 
						 .userId(userId)
						 .expired(false)
						 .build();
	}


	public void emailAuthentication(Long tokenId) {
		Optional<EmailAuthenticationToken> findEmailToken = Optional.ofNullable(emailRepository.findByIdAndExpirationDateAfterAndExpired(tokenId, LocalDateTime.now(), false)
																.orElseThrow(() -> new UsernameNotFoundException("테스트 익셉션 다른걸로 바꿔야함")));
		EmailAuthenticationToken emailAuthenticationToken = findEmailToken.get();
		emailAuthenticationToken.useToken();
		emailRepository.save(emailAuthenticationToken);
		
		
	}
	
	
	

}
