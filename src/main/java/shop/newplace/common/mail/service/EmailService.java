package shop.newplace.common.mail.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.advice.exception.NotFoundUsersException;
import shop.newplace.common.mail.SpringBootMail;
import shop.newplace.common.mail.model.entity.EmailAuthenticationToken;
import shop.newplace.common.mail.model.repository.EmailRepository;
import shop.newplace.common.util.CipherUtil;

@RequiredArgsConstructor
@Service
public class EmailService {
	private final EmailRepository emailRepository;
	
	private final SpringBootMail springBootMail;
	
	private final UsersRepository usersRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;
	
	@Transactional
	public void sendEmailAuthentication(Users users, String reciverEmail) {
		Optional<EmailAuthenticationToken> findToken = emailRepository.findByUserId(users.getId());
		if(findToken.isPresent()) {
			EmailAuthenticationToken oldToken = findToken.get();
			oldToken.useToken();
			emailRepository.save(oldToken);
		}
		EmailAuthenticationToken emailAuthenticationToken = emailRepository.save(createEmailAuthenticationToken(users.getId()));
		StringBuilder emailAuthenticationUrl = new StringBuilder();
		emailAuthenticationUrl.append("http://localhost:8080/email/authentication/");
//		emailAuthenticationUrl.append(findToken.getUserId());
		emailAuthenticationUrl.append(emailAuthenticationToken.getId());
		springBootMail.sendEmailAuthenticationEmail(users, emailAuthenticationUrl.toString());
	}
	
	private EmailAuthenticationToken createEmailAuthenticationToken(Long userId) {
		return EmailAuthenticationToken.builder()
					 	 .expirationDateTime(LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE)) 
						 .userId(userId)
						 .expired(false)
						 .build();
	}

	@Transactional
	public void emailAuthentication(Long tokenId) {
		Optional<EmailAuthenticationToken> findEmailToken = Optional.ofNullable(emailRepository.findByIdAndExpirationDateTimeAfterAndExpired(tokenId, LocalDateTime.now(), false)
																.orElseThrow(() -> new UsernameNotFoundException("테스트 익셉션 다른걸로 바꿔야함")));
		EmailAuthenticationToken emailAuthenticationToken = findEmailToken.get();
		emailAuthenticationToken.useToken();
		emailRepository.save(emailAuthenticationToken);
		Users users = usersRepository.findById(emailAuthenticationToken.getUserId())
									 .orElseThrow(() -> new NotFoundUsersException("해당 유저가 존재하지 않습니다.", emailAuthenticationToken.getUserId().toString()));
		users.emailAuthentication();
		usersRepository.save(users);
	}
	
	@Transactional
	public void sendTemporaryPassword(String loginEmail) {
		Users users = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(loginEmail))
				 					 .orElseThrow(() -> new NotFoundUsersException("해당 유저가 존재하지 않습니다.", loginEmail));
		String temporaryPassword = UUID.randomUUID().toString().replaceAll("-", "");
		temporaryPassword = temporaryPassword.substring(0, 10);
		users.changePassword(passwordEncoder.encode(temporaryPassword));
		usersRepository.save(users);
		springBootMail.sendTemporaryPasswordEmail(users, temporaryPassword);
	}
	
	

}
